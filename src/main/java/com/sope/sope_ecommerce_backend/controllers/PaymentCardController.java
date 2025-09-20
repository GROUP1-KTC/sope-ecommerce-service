package com.sope.sope_ecommerce_backend.controllers;
import com.sope.sope_ecommerce_backend.dto.request.AddPaymentCardRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentCardResponse;
import com.sope.sope_ecommerce_backend.security.user.CustomUserDetails;
import com.sope.sope_ecommerce_backend.services.PaymentCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payment-cards")
@RequiredArgsConstructor
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping
    public PaymentCardResponse addPaymentCard(
            @RequestBody AddPaymentCardRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = userDetails.getUserId();
        return paymentCardService.addPaymentCard(userId, request);
    }

    @GetMapping
    public List<PaymentCardResponse> getUserCards(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = userDetails.getUserId();
        return paymentCardService.getUserPaymentCards(userId);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCard(@PathVariable UUID cardId) {
        paymentCardService.deletePaymentCard(cardId);
    }

    @PutMapping("/default/{cardId}")
    public PaymentCardResponse setDefault(
            @PathVariable UUID cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = userDetails.getUserId();
        return paymentCardService.setDefaultCard(userId, cardId);
    }

}
