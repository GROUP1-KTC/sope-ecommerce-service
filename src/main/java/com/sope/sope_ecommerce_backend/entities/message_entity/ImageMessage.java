package com.sope.sope_ecommerce_backend.entities.message_entity;

import com.sope.sope_ecommerce_backend.entities.Message;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("IMAGE")
public class ImageMessage extends Message {
    @Column(name = "image_url")
    private String imageUrl;

    @Column
    private Integer width;

    @Column
    private Integer height;
}