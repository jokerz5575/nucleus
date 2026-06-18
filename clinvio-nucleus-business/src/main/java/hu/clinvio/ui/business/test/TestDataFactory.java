package hu.clinvio.ui.business.test;

import hu.clinvio.ui.persistence.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Utility class for creating test data.
 *
 * <pre>{@code
 * // Create test entity
 * Order order = TestDataFactory.createEntity(Order.class);
 *
 * // Create test page
 * Page<Order> page = TestDataFactory.createPage(orders, 0, 10);
 *
 * // Random string
 * String random = TestDataFactory.randomString(10);
 * }</pre>
 */
public class TestDataFactory {

    private TestDataFactory() {}

    /**
     * Create an instance of an entity class for testing.
     */
    public static <T extends BaseEntity> T createEntity(Class<T> entityClass) {
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            entity.setId(1L);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test entity: " + entityClass.getName(), e);
        }
    }

    /**
     * Create an entity with a specific ID.
     */
    public static <T extends BaseEntity> T createEntity(Class<T> entityClass, Long id) {
        T entity = createEntity(entityClass);
        entity.setId(id);
        return entity;
    }

    /**
     * Create a list of test entities.
     */
    public static <T extends BaseEntity> List<T> createEntities(Class<T> entityClass, int count) {
        List<T> entities = new ArrayList<>();
        for (long i = 1; i <= count; i++) {
            entities.add(createEntity(entityClass, i));
        }
        return entities;
    }

    /**
     * Create a Page from a list.
     */
    public static <T> Page<T> createPage(List<T> content, int page, int size) {
        return new PageImpl<>(content, PageRequest.of(page, size), content.size());
    }

    /**
     * Create an empty Page.
     */
    public static <T> Page<T> emptyPage(int page, int size) {
        return new PageImpl<>(List.of(), PageRequest.of(page, size), 0);
    }

    /**
     * Generate a random string.
     */
    public static String randomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    /**
     * Generate a random email.
     */
    public static String randomEmail() {
        return "test-" + randomString(8) + "@example.com";
    }

    /**
     * Generate a random number within a range.
     */
    public static int randomInt(int min, int max) {
        return min + (int) (Math.random() * (max - min + 1));
    }
}
