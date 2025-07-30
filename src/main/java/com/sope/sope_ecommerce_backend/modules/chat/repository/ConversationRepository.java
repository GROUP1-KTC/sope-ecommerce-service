package com.sope.sope_ecommerce_backend.modules.chat.repository;
import com.sope.sope_ecommerce_backend.modules.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, String> {

}
