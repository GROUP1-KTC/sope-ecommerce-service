package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.NotificationRequest;
import com.sope.sope_ecommerce_backend.dto.response.NotificationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserNotificationResponse;
import com.sope.sope_ecommerce_backend.entities.Notification;
import com.sope.sope_ecommerce_backend.entities.UserNotification;
import com.sope.sope_ecommerce_backend.mapper.NotificationMapper;
import com.sope.sope_ecommerce_backend.repositories.NotificationRepository;
import com.sope.sope_ecommerce_backend.repositories.UserNotificationRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .title(request.title())
                .message(request.message())
                .createdAt(Instant.now())
                .build();

        List<UserNotification> userNotifications = request.userIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .map(user -> UserNotification.builder()
                                .user(user)
                                .notification(notification)
                                .isRead(false)
                                .build())
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId)))
                .toList();

        notification.setUserNotifications(userNotifications);
        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toNotificationResponse(saved);
    }

    @Override
    public List<UserNotificationResponse> getUserNotifications(UUID userId) {
        List<UserNotification> list = userNotificationRepository.findByUserIdAndIsReadFalse(userId);
        return notificationMapper.toUserNotificationResponses(list);
    }

    @Override
    @Transactional
    public void markAsRead(UUID userNotificationId) {
        UserNotification userNotification = userNotificationRepository.findById(userNotificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found for user"));
        if (!userNotification.isRead()) {
            userNotification.setRead(true);
            userNotification.setReadAt(Instant.now());
            userNotificationRepository.save(userNotification);
        }
    }
}

