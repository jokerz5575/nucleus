package hu.clinvio.ui.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    private static class TestEntity extends BaseEntity {
    }

    @Test
    void prePersistSetsCreatedAtAndUpdatedAt() {
        TestEntity entity = new TestEntity();
        assertNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());

        entity.onCreate();

        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void preUpdateSetsUpdatedAt() {
        TestEntity entity = new TestEntity();
        entity.onCreate();
        LocalDateTime originalUpdatedAt = entity.getUpdatedAt();

        entity.onUpdate();

        assertNotNull(entity.getUpdatedAt());
        assertTrue(entity.getUpdatedAt().isAfter(originalUpdatedAt) || entity.getUpdatedAt().isEqual(originalUpdatedAt));
    }

    @Test
    void equalsHashCodeContract() {
        TestEntity entity1 = new TestEntity();
        entity1.setId(1L);
        TestEntity entity2 = new TestEntity();
        entity2.setId(1L);
        TestEntity entity3 = new TestEntity();
        entity3.setId(2L);

        assertEquals(entity1, entity1);
        assertEquals(entity1, entity2);
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1, entity3);
        assertNotEquals(entity2, entity3);
        assertNotEquals(entity1, null);
    }

    @Test
    void gettersSettersWork() {
        TestEntity entity = new TestEntity();
        entity.setId(42L);
        assertEquals(42L, entity.getId());

        LocalDateTime now = LocalDateTime.now();
        entity.setDeletedAt(now);
        assertEquals(now, entity.getDeletedAt());
        assertTrue(entity.isDeleted());
    }
}
