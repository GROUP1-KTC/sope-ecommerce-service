package com.sope.sope_ecommerce_backend.repositories;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findByChatUserAndShopOwnerUser(AppUser chatUser, AppUser shopOwnerUser);

    List<Conversation> findByChatUserOrShopOwnerUser(AppUser chatUser, AppUser shopOwnerUser);

}

