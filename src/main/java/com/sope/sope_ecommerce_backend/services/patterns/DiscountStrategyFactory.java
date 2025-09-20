package com.sope.sope_ecommerce_backend.services.patterns;

import com.sope.sope_ecommerce_backend.enums.DiscountScope;
import org.springframework.stereotype.Component;

@Component
public class DiscountStrategyFactory {
    public DiscountStrategy getStrategy(DiscountScope scope) {
        return switch (scope) {
            case SHOP -> new ShopDiscountStrategy();
            case PLATFORM -> new PlatformDiscountStrategy();
            case FREESHIP -> new FreeShipDiscountStrategy();
            case COIN_BACK -> new CoinBackDiscountStrategy();
            default -> throw new IllegalArgumentException("Unsupported discount scope: " + scope);
        };
    }
}