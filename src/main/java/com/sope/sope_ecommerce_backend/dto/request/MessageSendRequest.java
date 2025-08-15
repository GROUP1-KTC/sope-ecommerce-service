package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record MessageSendRequest(
        UUID conversationId,
        UUID senderId,
        String type,
        String content,
        String imageUrl,
        Integer width,
        Integer height,
        String fileUrl,
        String fileName,
        String fileType,
        Long fileSize
) {
}
