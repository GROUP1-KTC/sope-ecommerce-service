    package com.sope.sope_ecommerce_backend.entities;

    import com.sope.sope_ecommerce_backend.enums.Gender;
    import com.sope.sope_ecommerce_backend.enums.UserStatus;
    import jakarta.persistence.*;
    import lombok.*;

    import java.util.*;

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
        private String phone;
        private String email;
        private String avatarUrl;

        @Enumerated(EnumType.STRING)
        private Gender gender;

        private String note;

        @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private List<Address> addresses = new ArrayList<>();

        @Enumerated(EnumType.STRING)
        private UserStatus status = UserStatus.INACTIVE;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        @Builder.Default
        private Set<UserRole> userRoles = new HashSet<>();

        @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private UserSetting setting;

    }
