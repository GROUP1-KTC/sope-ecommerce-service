package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.ConversationCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationService {
    ConversationResponse createConversation (ConversationCreateRequest request);
    List<ConversationResponse> getConversationById(UUID conversationId);
    List<ConversationResponse> getUserConversations();
//    void deleteConversation(UUID conversationId, UUID requesterId);
}
