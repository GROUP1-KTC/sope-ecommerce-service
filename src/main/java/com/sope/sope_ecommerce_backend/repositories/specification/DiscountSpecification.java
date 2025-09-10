package com.sope.sope_ecommerce_backend.repositories.specification;

import com.sope.sope_ecommerce_backend.entities.Discount;
import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.UUID;

public class DiscountSpecification {
    public static Specification<Discount> byShopId(UUID shopId) {
        return (root, query, cb) ->
                cb.equal(root.get("shop").get("id"), shopId);
    }


    public static Specification<Discount> byScopes(DiscountScope... scopes) {
        return (root, query, cb) -> root.get("scope").in((Object[]) scopes);
    }

    public static Specification<Discount> isActive(LocalDateTime now) {
        return (root, query, cb) -> cb.and(
                cb.or(
                        cb.isNull(root.get("startDate")),
                        cb.lessThanOrEqualTo(root.get("startDate"), now)
                ),
                cb.or(
                        cb.isNull(root.get("endDate")),
                        cb.greaterThanOrEqualTo(root.get("endDate"), now)
                ),
                cb.or(
                        cb.equal(root.get("maxUsage"), 0),
                        cb.lessThan(root.get("currentUsage"), root.get("maxUsage"))
                )
        );
    }
}
