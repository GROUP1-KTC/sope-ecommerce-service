package com.sope.sope_ecommerce_backend.events;
import com.sope.sope_ecommerce_backend.dto.request.OrderEmailRequest;
import com.sope.sope_ecommerce_backend.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentEmailListener {
    private final EmailService emailService;

    @Async
    @EventListener
    public void handlePaymentCreated(PaymentCreatedEvent event) {

        if (event.ordersForEmail().isEmpty()) return;

        OrderEmailRequest firstOrder = event.ordersForEmail().get(0);

        Map<String, Object> emailModel = new HashMap<>();
        emailModel.put("user", Map.of(
                "name", firstOrder.guestName(),
                "email", firstOrder.guestEmail(),
                "phone", firstOrder.guestPhone(),
                "address", firstOrder.guestAddress()
        ));
        emailModel.put("orders", event.ordersForEmail());
        emailModel.put("grandTotal", event.ordersForEmail().stream()
                .map(OrderEmailRequest::totalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        emailModel.put("paymentUrl", event.paymentResponse().getPayUrl());

        try {
            emailService.sendOrderConfirmationEmail(firstOrder.guestEmail(), emailModel);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }
}
