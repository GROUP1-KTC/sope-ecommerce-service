// package com.sope.sope_ecommerce_backend.repository;

// import com.sope.sope_ecommerce_backend.entities.AppUser;
// import com.sope.sope_ecommerce_backend.entities.Cart;
// import com.sope.sope_ecommerce_backend.entities.Shop.Status;
// import com.sope.sope_ecommerce_backend.enums.UserStatus;
// import com.sope.sope_ecommerce_backend.repositories.CartRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
// import org.springframework.test.context.ActiveProfiles;

// import java.util.Optional;
// import java.util.Set;

// import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
// @ActiveProfiles("test")
// public class CartRepositoryTest {
// @Autowired
// TestEntityManager testEntityManager;
// @Autowired
// CartRepository cartRepository;

// private AppUser appUser1;
// private Cart cart1;

// @BeforeEach
// void setUp() {
// appUser1 = AppUser.builder()
// .email("test@example.com")
// .password("password123")
// .username("testuser")
// .name("Test User")
// .phone("1234567890")
// .address("123 Test St")
// .note("Test note")
// .status(UserStatus.ACTIVE)
// .userRoles(Set.of())
// .build();
// testEntityManager.persist(appUser1);

// cart1 = Cart.builder()
// .appUser(appUser1)
// .build();
// testEntityManager.persist(cart1);
// testEntityManager.flush();
// }

// @Test
// void findByUser_shouldReturnCart_whenUserExists() {
// Optional<Cart> foundCart = cartRepository.findByAppUser(appUser1);

// assertThat(foundCart).isPresent();
// assertThat(foundCart.get().getAppUser()).isEqualTo(appUser1);
// }

// @Test
// void findByUser_shouldReturnEmpty_whenUserDoesNotExist() {
// AppUser anotherAppUser = AppUser.builder()
// .email("nouser@example.com")
// .password("pass")
// .build();
// testEntityManager.persist(anotherAppUser);

// Optional<Cart> foundCart = cartRepository.findByAppUser(anotherAppUser);

// assertThat(foundCart).isEmpty();
// }
// }
