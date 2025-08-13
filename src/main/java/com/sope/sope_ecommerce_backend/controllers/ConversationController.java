package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.ConversationCreateRequest;
import com.sope.sope_ecommerce_backend.dto.response.ConversationResponse;
import com.sope.sope_ecommerce_backend.services.impl.ConversationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConversationController {
    private final ConversationServiceImpl conversationService;

    public ConversationController(ConversationServiceImpl conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(@RequestBody ConversationCreateRequest request) {
        return ResponseEntity.ok(conversationService.createConversation(request));
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getUserConversations() {
        return ResponseEntity.ok(conversationService.getUserConversations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<ConversationResponse>> getConversationById(@PathVariable UUID id) {
        return ResponseEntity.ok(conversationService.getConversationById(id));
    }
}