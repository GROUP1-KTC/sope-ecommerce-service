package com.sope.sope_ecommerce_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_settings")
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Boolean orderUpdateNoti;
    private Boolean promotionNoti;
    private Boolean surveyNoti;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private AppUser user;
}

