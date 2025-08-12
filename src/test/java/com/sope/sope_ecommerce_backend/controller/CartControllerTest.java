package com.sope.sope_ecommerce_backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.controllers.CartController;
import com.sope.sope_ecommerce_backend.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;

    @MockitoBean
    CartService cartService;
}
