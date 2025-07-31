package com.sope.sope_ecommerce_backend.repositories;
import com.sope.sope_ecommerce_backend.entities.MessageEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, String> {
    List<MessageEntity> findByConversationId(String conversationId);
}