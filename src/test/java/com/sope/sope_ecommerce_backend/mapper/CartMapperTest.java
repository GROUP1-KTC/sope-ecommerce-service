package com.sope.sope_ecommerce_backend.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class CartMapperTest {
    private CartMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CartMapper.class);
    }


    @Test
    void toDto() {
        // Implement test logic here
    }
}
