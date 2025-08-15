package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;

public record MessageResponse(
        UUID id,
        UUID senderId,
        String type,
        String content,
        String imageUrl,
        Integer width,
        Integer height,
        String fileUrl,
        String fileName,
        String fileType,
        Long fileSize,
        String sentAt
) {
}
