package com.sope.sope_ecommerce_backend.mapper;

import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MessageMapper.class})
public interface ConversationMapper {

    @Mapping(target = "conversationId", source = "id")
    @Mapping(target = "lastMessage", source = "lastMessage")
    @Mapping(target = "lastSenderId", source = "lastSenderId")
    ConversationResponse toResponse(Conversation conversation);
}
