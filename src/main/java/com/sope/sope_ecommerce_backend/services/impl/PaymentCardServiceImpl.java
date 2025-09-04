package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.AddPaymentCardRequest;
import com.sope.sope_ecommerce_backend.dto.response.PaymentCardResponse;
import com.sope.sope_ecommerce_backend.entities.AppUser;
import com.sope.sope_ecommerce_backend.entities.PaymentCard;
import com.sope.sope_ecommerce_backend.repositories.PaymentCardRepository;
import com.sope.sope_ecommerce_backend.repositories.UserRepository;
import com.sope.sope_ecommerce_backend.services.PaymentCardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;
    private final UserRepository appUserRepository;

    private String generateFakeToken() {
        return "tok_" + UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public PaymentCardResponse addPaymentCard(UUID userId, AddPaymentCardRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        String last4Digits = request.cardNumber().substring(request.cardNumber().length() - 4);

        PaymentCard card = PaymentCard.builder()
                .cardHolderName(request.cardHolderName())
                .last4Digits(last4Digits)
                .cardType(request.cardType())
                .expiryDate(request.expiryDate())
                .token(generateFakeToken())
                .appUser(user)
                .build();

        return toResponse(paymentCardRepository.save(card));
    }

    @Override
    public List<PaymentCardResponse> getUserPaymentCards(UUID userId) {
        return paymentCardRepository.findByAppUser_Id(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePaymentCard(UUID cardId) {
        paymentCardRepository.deleteById(cardId);
    }

    @Override
    public PaymentCardResponse setDefaultCard(UUID userId, UUID cardId) {
        List<PaymentCard> cards = paymentCardRepository.findByAppUser_Id(userId);
        for (PaymentCard card : cards) {
            card.setIsDefault(card.getId().equals(cardId));
        }
        paymentCardRepository.saveAll(cards);
        return toResponse(paymentCardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found")));
    }

    private PaymentCardResponse toResponse(PaymentCard card) {
        return new PaymentCardResponse(
                card.getId(),
                card.getCardHolderName(),
                card.getLast4Digits(),
                card.getCardType(),
                card.getExpiryDate(),
                card.getIsDefault()
        );
    }
}

