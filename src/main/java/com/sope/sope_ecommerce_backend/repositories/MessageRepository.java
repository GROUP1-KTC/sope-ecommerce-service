package com.sope.sope_ecommerce_backend.repositories;

import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByConversationOrderBySentAtAsc(Conversation conversation);

}
