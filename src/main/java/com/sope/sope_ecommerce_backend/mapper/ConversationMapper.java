package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapper.class})
public interface ConversationMapper {

    @Mapping(target = "conversationId", source = "conversation.id")   // ✅ fix
    @Mapping(target = "lastMessage", source = "conversation.lastMessage")
    @Mapping(target = "lastSenderId", source = "conversation.lastSenderId")
    @Mapping(target = "name", expression = "java(mapName(conversation, currentUserId))")
    ConversationResponse toResponse(Conversation conversation, UUID currentUserId);

    default String mapName(Conversation conversation, UUID currentUserId) {
        if (conversation.getParticipants() != null && conversation.getParticipants().size() == 2) {
            return conversation.getParticipants().stream()
                    .filter(p -> !p.getId().equals(currentUserId))
                    .map(u -> u.getUsername())
                    .findFirst()
                    .orElse("Unknown");
        } else {
            return "Group Chat";
        }
    }
}

