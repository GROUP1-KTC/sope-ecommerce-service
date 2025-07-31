package com.sope.sope_ecommerce_backend.repositories;
import com.sope.sope_ecommerce_backend.entities.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationEntity, String> {

}
