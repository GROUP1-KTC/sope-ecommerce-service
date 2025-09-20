package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapper.class})
public interface ConversationMapper {

    @Mapping(target = "conversationId", source = "conversation.id")
    @Mapping(target = "lastMessage", source = "conversation.lastMessage")
    @Mapping(target = "lastSenderId", source = "conversation.lastSenderId")
    @Mapping(target = "name", expression = "java(mapName(conversation, currentUserId))")
    ConversationResponse toResponse(Conversation conversation, UUID currentUserId);

    default String mapName(Conversation conversation, UUID currentUserId) {
        // Nếu currentUser là chatUser, hiển thị tên shopOwner
        if (conversation.getChatUser() != null && conversation.getShopOwnerUser() != null) {
            if (conversation.getChatUser().getId().equals(currentUserId)) {
                return conversation.getShopOwnerUser().getUsername();
            } else {
                return conversation.getChatUser().getUsername();
            }
        }
        return "Unknown";
    }
}
