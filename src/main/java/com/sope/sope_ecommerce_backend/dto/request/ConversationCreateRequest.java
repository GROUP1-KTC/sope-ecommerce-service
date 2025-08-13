package com.sope.sope_ecommerce_backend.dto.request;

import java.util.UUID;

public record ConversationCreateRequest(
        UUID user1,
        UUID user2
) {
}
