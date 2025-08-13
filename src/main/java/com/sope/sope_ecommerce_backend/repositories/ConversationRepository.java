package com.sope.sope_ecommerce_backend.repositories;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    @Query("""
        SELECT c FROM Conversation c
        JOIN c.participants p
        WHERE p.id IN (:user1, :user2)
        GROUP BY c.id
        HAVING COUNT(DISTINCT p.id) = 2
    """)
    Optional<Conversation> findByParticipants(@Param("user1") UUID user1, @Param("user2") UUID user2);

    List<Conversation> findByParticipants_Id(UUID userId);


}

