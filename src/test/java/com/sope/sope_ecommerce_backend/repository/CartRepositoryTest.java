package com.sope.sope_ecommerce_backend.repository;


import com.sope.sope_ecommerce_backend.entities.Cart;
import com.sope.sope_ecommerce_backend.entities.User;
import com.sope.sope_ecommerce_backend.repositories.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CartRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    CartRepository cartRepository;

    private User user1;
    private Cart cart1;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .email("test@example.com")
                .password("password123")
                .username("testuser")
                .name("Test User")
                .phone("1234567890")
                .address("123 Test St")
                .note("Test note")
                .status("ACTIVE")
                .roles(Set.of())
                .build();
        testEntityManager.persist(user1);

        cart1 = Cart.builder()
                .user(user1)
                .build();
        testEntityManager.persist(cart1);
        testEntityManager.flush();
    }

    @Test
    void findByUser_shouldReturnCart_whenUserExists() {
        Optional<Cart> foundCart = cartRepository.findByUser(user1);

        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getUser()).isEqualTo(user1);
    }


    @Test
    void findByUser_shouldReturnEmpty_whenUserDoesNotExist() {
        User anotherUser = User.builder()
                .email("nouser@example.com")
                .password("pass")
                .build();
        testEntityManager.persist(anotherUser);

        Optional<Cart> foundCart = cartRepository.findByUser(anotherUser);

        assertThat(foundCart).isEmpty();
    }
}
