package com.sope.sope_ecommerce_backend.repositories.specification;

import com.sope.sope_ecommerce_backend.entities.CartItem;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CartItemSpecification {
    /**
     * Specification to fetch a CartItem by user ID and product variant ID.
     * This will also fetch the associated Cart and AppUser entities.
     *
     * @param userId     the ID of the user
     * @param variantId  the ID of the product variant
     * @return a Specification for the CartItem
     */
    public static Specification<CartItem> byUserAndVariantFetch(UUID userId, UUID variantId) {
        return (root, query, cb) -> {
            root.fetch("cart", JoinType.LEFT)
                    .fetch("appUser", JoinType.LEFT);
            root.fetch("productVariant", JoinType.LEFT);
            query.distinct(true);
            return cb.and(
                    cb.equal(root.join("cart").join("appUser").get("id"), userId),
                    cb.equal(root.join("productVariant").get("id"), variantId)
            );
        };
    }

    /**
     * Specification to fetch a CartItem by user ID and cart item ID.
     * This will also fetch the associated Cart and AppUser entities.
     *
     * @param userId       the ID of the user
     * @param cartItemId   the ID of the cart item
     * @return a Specification for the CartItem
     */
    public static Specification<CartItem> byUserAndItemIdFetch(UUID userId, UUID cartItemId) {
        return (root, query, cb) -> {
            root.fetch("cart", JoinType.LEFT).fetch("appUser", JoinType.LEFT);
            root.fetch("productVariant", JoinType.LEFT);
            query.distinct(true);
            return cb.and(
                    cb.equal(root.join("cart").join("appUser").get("id"), userId),
                    cb.equal(root.get("id"), cartItemId)
            );
        };
    }
}
