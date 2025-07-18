package com.sope.sope_ecommerce_backend.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFollowerId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "follower_id")
    private UUID followerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowerId)) return false;
        UserFollowerId that = (UserFollowerId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(followerId, that.followerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, followerId);
    }
}
