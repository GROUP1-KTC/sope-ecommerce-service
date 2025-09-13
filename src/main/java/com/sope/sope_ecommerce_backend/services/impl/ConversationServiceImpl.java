package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.ConversationCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.mapper.ConversationMapper;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.ConversationService;
import com.sope.sope_ecommerce_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final ConversationMapper conversationMapper;
    private final UserService userService;
    private final ShopRepository shopRepository;

    @Override
    public ConversationResponse createConversation(ConversationCreateRequest request) {
        UUID currentUserId = userService.getCurrentUserId();

        AppUser chatUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Shop shop = shopRepository.findById(request.shopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        AppUser shopOwnerUser = shop.getAppUser();

        return conversationRepository.findByChatUserAndShopOwnerUser(chatUser, shopOwnerUser)
                .map(conv -> conversationMapper.toResponse(conv, currentUserId))
                .orElseGet(() -> {
                    Conversation newConversation = new Conversation();
                    newConversation.setChatUser(chatUser);
                    newConversation.setShopOwnerUser(shopOwnerUser);
                    return conversationMapper.toResponse(conversationRepository.save(newConversation), currentUserId);
                });
    }


    @Override
    public List<ConversationResponse> getUserConversations() {
        UUID currentUserId = userService.getCurrentUserId();
        AppUser currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return conversationRepository.findByChatUserOrShopOwnerUser(currentUser, currentUser)
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
