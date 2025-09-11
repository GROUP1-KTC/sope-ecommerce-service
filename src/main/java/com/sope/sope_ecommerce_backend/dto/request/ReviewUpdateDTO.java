package com.sope.sope_ecommerce_backend.dto.request;

import java.util.List;

public record ReviewUpdateDTO(
    Integer rating,
    String content,
    List<String> imageUrlsToKeep) {
}
