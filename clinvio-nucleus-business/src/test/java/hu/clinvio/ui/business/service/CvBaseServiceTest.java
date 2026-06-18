package hu.clinvio.ui.business.service;

import hu.clinvio.ui.business.repository.CvBaseRepository;
import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvBaseServiceTest {

    private static class TestEntity extends BaseEntity {
    }

    private static class TestService extends CvBaseService<TestEntity, Long> {
        protected TestService(CvBaseRepository<TestEntity, Long> repository) {
            super(repository);
        }
    }

    @Mock
    private CvBaseRepository<TestEntity, Long> repository;

    private TestService service;

    @BeforeEach
    void setUp() {
        service = new TestService(repository);
    }

    @Test
    void findAllDelegatesToRepo() {
        List<TestEntity> entities = List.of(new TestEntity(), new TestEntity());
        when(repository.findAllActive()).thenReturn(entities);

        List<TestEntity> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository).findAllActive();
    }

    @Test
    void findByIdReturnsOptional() {
        TestEntity entity = new TestEntity();
        entity.setId(1L);
        when(repository.findActiveById(1L)).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findAllPageDelegatesToRepo() {
        Pageable pageable = Pageable.ofSize(10);
        Page<TestEntity> page = new PageImpl<>(List.of(new TestEntity()));
        when(repository.findAllActive(pageable)).thenReturn(page);

        Page<TestEntity> result = service.findAll(pageable);

        assertFalse(result.isEmpty());
        verify(repository).findAllActive(pageable);
    }
}
