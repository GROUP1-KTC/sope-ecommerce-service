package com.sope.sope_ecommerce_backend.dto.response;

import com.sope.sope_ecommerce_backend.entities.Message;

import java.util.List;
import java.util.UUID;

public record ConversationResponse(
        UUID conversationId,
        List<UserResponse> participants,
        Message lastMessage,
        UUID lastSenderId
) {
}
