package com.sope.sope_ecommerce_backend.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.response.LiveEventMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("Redis event received on channel {}: {}", channel, body);

        try {
            switch (channel) {
                case "livestream.events" -> handleLiveStreamEvent(body);
                // case "chat.events" -> handleChatEvent(body);
                // case "notification.events" -> handleNotificationEvent(body);
                default -> log.warn("No handler registered for channel: {}", channel);
            }
        } catch (Exception e) {
            log.error("Failed to handle event from channel {}", channel, e);
        }
    }

    // ------------ Handlers riêng cho từng channel ----------------
    private void handleLiveStreamEvent(String body) {
        try {
            LiveEventMessage evt = objectMapper.readValue(body, LiveEventMessage.class);
            messagingTemplate.convertAndSend("/topic/livestreams", evt);
        } catch (Exception e) {
            log.error("Failed to handle LiveStream event", e);
        }
    }

//    private void handleChatEvent(String body) {
//        try {
//            ChatEvent evt = objectMapper.readValue(body, ChatEvent.class);
//            messagingTemplate.convertAndSend("/topic/chats/" + evt.getRoomId(), evt);
//        } catch (Exception e) {
//            log.error("Failed to handle Chat event", e);
//        }
//    }
//
//    private void handleNotificationEvent(String body) {
//        try {
//            NotificationEvent evt = objectMapper.readValue(body, NotificationEvent.class);
//            messagingTemplate.convertAndSend("/topic/notifications/" + evt.getUserId(), evt);
//        } catch (Exception e) {
//            log.error("Failed to handle Notification event", e);
//        }
//    }
}