package hu.clinvio.ui.business.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CvSimpleNotificationService {

    private static final Logger log = LoggerFactory.getLogger(CvSimpleNotificationService.class);
    private final Map<String, CvNotification> notifications = new ConcurrentHashMap<>();

    public void send(CvNotification notification) {
        notifications.put(notification.getId(), notification);
        log.debug("Notification sent: {} to {}", notification.getTitle(), notification.getRecipientId());
    }

    public void send(String recipientId, String title, String message, CvNotification.NotificationType type) {
        send(CvNotification.builder()
                .recipientId(recipientId)
                .title(title)
                .message(message)
                .type(type)
                .build());
    }

    public void sendSuccess(String recipientId, String title, String message) {
        send(recipientId, title, message, CvNotification.NotificationType.SUCCESS);
    }

    public void sendWarning(String recipientId, String title, String message) {
        send(recipientId, title, message, CvNotification.NotificationType.WARNING);
    }

    public void sendError(String recipientId, String title, String message) {
        send(recipientId, title, message, CvNotification.NotificationType.ERROR);
    }

    public List<CvNotification> getUnread(String recipientId) {
        return notifications.values().stream()
                .filter(n -> recipientId.equals(n.getRecipientId()) && !n.isRead())
                .sorted(Comparator.comparing(CvNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public List<CvNotification> getAll(String recipientId) {
        return notifications.values().stream()
                .filter(n -> recipientId.equals(n.getRecipientId()))
                .sorted(Comparator.comparing(CvNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    public int getUnreadCount(String recipientId) {
        return (int) notifications.values().stream()
                .filter(n -> recipientId.equals(n.getRecipientId()) && !n.isRead())
                .count();
    }

    public void markAsRead(String notificationId) {
        CvNotification notification = notifications.get(notificationId);
        if (notification != null) {
            notification.setRead(true);
        }
    }

    public void markAllAsRead(String recipientId) {
        notifications.values().stream()
                .filter(n -> recipientId.equals(n.getRecipientId()))
                .forEach(n -> n.setRead(true));
    }

    public void delete(String notificationId) {
        notifications.remove(notificationId);
    }

    public void deleteAll(String recipientId) {
        notifications.entrySet().removeIf(entry -> recipientId.equals(entry.getValue().getRecipientId()));
    }
}
