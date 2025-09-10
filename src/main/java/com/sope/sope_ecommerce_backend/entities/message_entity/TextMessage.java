package com.sope.sope_ecommerce_backend.entities.message_entity;

import com.sope.sope_ecommerce_backend.entities.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("TEXT")
@Schema(description = "Text message object")
public class TextMessage extends Message {
    @Column(nullable = false)
    private String content;
}