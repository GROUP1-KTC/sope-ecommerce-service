    package com.sope.sope_ecommerce_backend.entities;

    import com.sope.sope_ecommerce_backend.enums.UserStatus;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.HashSet;
    import java.util.Set;
    import java.util.UUID;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "users")
    public class AppUser {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;
        private String username;
        private String password;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String note;

        @Enumerated(EnumType.STRING)
        private UserStatus status = UserStatus.INACTIVE;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<UserRole> userRoles = new HashSet<>();
    }
