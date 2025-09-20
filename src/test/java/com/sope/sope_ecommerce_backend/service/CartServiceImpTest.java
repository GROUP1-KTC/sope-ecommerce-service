package com.sope.sope_ecommerce_backend.service;

import com.sope.sope_ecommerce_backend.dto.response.CartItemResponse;
import com.sope.sope_ecommerce_backend.mapper.CartMapper;
import com.sope.sope_ecommerce_backend.repositories.CartRepository;
import com.sope.sope_ecommerce_backend.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceImpTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    CartMapper cartMapper;

    CartServiceImpl cartService;

    CartItemResponse cartItemResponse;

    @BeforeEach
    void setUp() {

    }
}
