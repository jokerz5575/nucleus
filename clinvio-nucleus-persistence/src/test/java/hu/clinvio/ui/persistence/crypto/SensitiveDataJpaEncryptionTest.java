package hu.clinvio.ui.persistence.crypto;

import hu.clinvio.ui.persistence.annotation.SensitiveData;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@EntityScan(basePackageClasses = SensitiveDataJpaEncryptionTest.EncryptedRecord.class)
@Import(SensitiveDataJpaEncryptionTest.CryptoTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class SensitiveDataJpaEncryptionTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        new EncryptedStringConverter().setCryptoService(new AesCryptoService("jpa-encryption-test-key"));
    }

    @Test
    void sensitiveDataFieldWithExplicitConvertStoresCiphertext() {
        EncryptedRecord saved = entityManager.persistAndFlush(new EncryptedRecord("secret diagnosis"));

        String rawValue = jdbcTemplate.queryForObject(
                "select secret_note from cv_encrypted_record where id = ?",
                String.class,
                saved.getId());

        assertNotEquals("secret diagnosis", rawValue);
        assertTrue(new AesCryptoService("jpa-encryption-test-key").isEncrypted(rawValue));

        entityManager.clear();
        EncryptedRecord loaded = entityManager.find(EncryptedRecord.class, saved.getId());

        assertEquals("secret diagnosis", loaded.getSecretNote());
    }

    @Test
    void sensitiveDataAnnotationDoesNotDeclareJpaConvertMetaAnnotation() {
        assertFalse(SensitiveData.class.isAnnotationPresent(Convert.class));
    }

    @Test
    void nullSensitiveDataPersistsAsNull() {
        EncryptedRecord saved = entityManager.persistAndFlush(new EncryptedRecord(null));

        String rawValue = jdbcTemplate.queryForObject(
                "select secret_note from cv_encrypted_record where id = ?",
                String.class,
                saved.getId());

        assertNull(rawValue);
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    static class CryptoTestConfig {
    }

    @Entity
    @Table(name = "cv_encrypted_record")
    static class EncryptedRecord {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @SensitiveData
        @Convert(converter = EncryptedStringConverter.class)
        @Column(name = "secret_note")
        private String secretNote;

        protected EncryptedRecord() {
        }

        EncryptedRecord(String secretNote) {
            this.secretNote = secretNote;
        }

        Long getId() {
            return id;
        }

        String getSecretNote() {
            return secretNote;
        }
    }
}
