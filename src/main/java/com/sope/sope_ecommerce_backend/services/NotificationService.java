package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.NotificationRequest;
import com.sope.sope_ecommerce_backend.dto.response.NotificationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserNotificationResponse;
import com.sope.sope_ecommerce_backend.entities.Notification;
import com.sope.sope_ecommerce_backend.entities.UserNotification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationResponse createNotification(NotificationRequest notificationRequest);
    List<UserNotificationResponse> getUserNotifications(UUID userId);
    void markAsRead(UUID userNotificationId);
}

