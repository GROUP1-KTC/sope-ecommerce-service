package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.MessageSendRequest;
import com.sope.sope_ecommerce_backend.dto.response.MessageResponse;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponse sendMessage(MessageSendRequest request);
    List<MessageResponse> getMessagesByConversation(String conversationId);
    void deleteMessage(UUID messageId);
}
