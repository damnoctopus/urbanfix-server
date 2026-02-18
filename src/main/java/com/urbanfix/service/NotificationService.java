package com.urbanfix.service;

import com.urbanfix.dto.NotificationResponse;
import com.urbanfix.entity.Notification;
import com.urbanfix.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationResponse createNotification(Long userId, String title, String body) {
        Notification notification = Notification.builder()
                .userId(userId)
                .title(title)
                .body(body)
                .build();
        return map(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::map)
                .toList();
    }

    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        // If Notification had a 'read' field we'd update it here; return mapped response
        return map(notification);
    }

    private NotificationResponse map(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
