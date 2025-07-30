package com.sope.sope_ecommerce_backend.modules.chat.repository;
import com.sope.sope_ecommerce_backend.modules.chat.entity.Message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByConversationId(String conversationId);
}