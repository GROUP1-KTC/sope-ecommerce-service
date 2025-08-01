package com.sope.sope_ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

import com.sope.sope_ecommerce_backend.enums.TargetType;

public record ComplaintRequestDTO(
            @NotNull UUID userId,
            @NotNull UUID targetId,
            @NotNull TargetType targetType,
            @NotBlank String description) {
}