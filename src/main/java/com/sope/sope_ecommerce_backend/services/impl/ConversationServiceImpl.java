package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.ConversationCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.mapper.ConversationMapper;
import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.ConversationService;
import com.sope.sope_ecommerce_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConversationMapper conversationMapper;
    private final UserService userService;


    @Override
    public ConversationResponse createConversation(ConversationCreateRequest request) {
        AppUser u1 = userRepository.findById(request.user1())
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        AppUser u2 = userRepository.findById(request.user2())
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        UUID currentUserId = userService.getCurrentUserId();

        return conversationRepository.findByParticipants(u1.getId(), u2.getId())
                .map(conv -> conversationMapper.toResponse(conv, currentUserId))
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setParticipants(List.of(u1, u2));
                    return conversationMapper.toResponse(conversationRepository.save(newConversation), currentUserId);
                });
    }

    @Override
    public List<ConversationResponse> getUserConversations() {
        UUID currentUserId = userService.getCurrentUserId();

        return conversationRepository.findByParticipants_Id(currentUserId)
                .stream()
                .map(conv -> conversationMapper.toResponse(conv, currentUserId))
                .toList();
    }

    @Override
    public List<ConversationResponse> getConversationById(UUID conversationId) {
        UUID currentUserId = userService.getCurrentUserId();

        return conversationRepository.findById(conversationId)
                .stream()
                .map(conv -> conversationMapper.toResponse(conv, currentUserId))
                .toList();
    }
}
