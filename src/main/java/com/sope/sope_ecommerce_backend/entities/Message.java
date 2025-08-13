package com.sope.sope_ecommerce_backend.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sope.sope_ecommerce_backend.entities.message_entity.FileMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.ImageMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.TextMessage;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "message_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMessage.class, name = "text"),
        @JsonSubTypes.Type(value = ImageMessage.class, name = "image"),
        @JsonSubTypes.Type(value = FileMessage.class, name = "file")
})
@Table(name = "messages")
public abstract class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sender;

    @Column(nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

    @OneToOne(mappedBy = "lastMessage")
    private Conversation conversationWithLastMessage;
}