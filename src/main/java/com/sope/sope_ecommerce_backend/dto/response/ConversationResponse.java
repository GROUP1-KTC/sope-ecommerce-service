package com.sope.sope_ecommerce_backend.dto.response;


import java.util.List;
import java.util.UUID;

public record ConversationResponse(
        UUID conversationId,
        List<UserResponse> participants,
        MessageResponse lastMessage,
        UUID lastSenderId,
        String name
) {
}
