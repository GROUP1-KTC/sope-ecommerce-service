package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.NotificationResponse;
import com.sope.sope_ecommerce_backend.dto.response.UserNotificationResponse;
import com.sope.sope_ecommerce_backend.entities.Notification;
import com.sope.sope_ecommerce_backend.entities.UserNotification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
    List<NotificationResponse> toNotificationResponses(List<Notification> notifications);
    UserNotificationResponse toUserNotificationResponse(UserNotification userNotification);
    List<UserNotificationResponse> toUserNotificationResponses(List<UserNotification> userNotifications);
}

