package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.ConversationCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.User;
import com.sope.sope_ecommerce_backend.mapper.ConversationMapper;
import com.sope.sope_ecommerce_backend.mapper.UserMapper;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.security.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.ConversationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConversationMapper conversationMapper;

    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   UserRepository userRepository, UserMapper userMapper, ConversationMapper conversationMapper) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.conversationMapper = conversationMapper;
    }

    @Override
    public ConversationResponse createConversation(ConversationCreateRequest request) {
        User u1 = userRepository.findById(request.user1())
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        User u2 = userRepository.findById(request.user2())
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        return conversationRepository.findByParticipants(u1.getId(), u2.getId())
                .map(conversationMapper::toResponse)
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setParticipants(List.of(u1, u2));
                    return conversationMapper.toResponse(conversationRepository.save(newConversation));
                });
    }

    @Override
    public List<ConversationResponse> getUserConversations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User chưa đăng nhập");
        }

        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return conversationRepository.findByParticipants_Id(user.getUserId())
                .stream()
                .map(conversationMapper::toResponse)
                .toList();
    }

    @Override
    public List<ConversationResponse> getConversationById(UUID conversationId) {
        return conversationRepository.findById(conversationId)
                .stream()
                .map(conversationMapper::toResponse)
                .toList();
    }
}
