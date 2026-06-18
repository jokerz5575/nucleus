package hu.clinvio.ui.business.notification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CvSimpleNotificationServiceTest {

    private CvSimpleNotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new CvSimpleNotificationService();
    }

    @Test
    void storeAndRetrieveNotifications() {
        CvNotification notification = CvNotification.builder()
                .recipientId("user1")
                .title("Test")
                .message("Hello")
                .type(CvNotification.NotificationType.INFO)
                .build();

        notificationService.send(notification);

        assertEquals(1, notificationService.getAll("user1").size());
        assertEquals("Test", notificationService.getAll("user1").get(0).getTitle());
    }

    @Test
    void getAllReturnsStoredNotifications() {
        notificationService.send("user1", "Title1", "Msg1", CvNotification.NotificationType.INFO);
        notificationService.send("user1", "Title2", "Msg2", CvNotification.NotificationType.SUCCESS);

        assertEquals(2, notificationService.getAll("user1").size());
    }

    @Test
    void clearRemovesAll() {
        notificationService.send("user1", "T", "M", CvNotification.NotificationType.INFO);
        notificationService.deleteAll("user1");

        assertTrue(notificationService.getAll("user1").isEmpty());
    }

    @Test
    void getByUserReturnsCorrectOnes() {
        notificationService.send("user1", "U1", "M1", CvNotification.NotificationType.INFO);
        notificationService.send("user2", "U2", "M2", CvNotification.NotificationType.WARNING);

        assertEquals(1, notificationService.getAll("user1").size());
        assertEquals(1, notificationService.getAll("user2").size());
        assertEquals("U1", notificationService.getAll("user1").get(0).getTitle());
    }
}
