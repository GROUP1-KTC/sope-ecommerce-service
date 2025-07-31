package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.response.ConversationDTO;
import com.sope.sope_ecommerce_backend.services.impl.ConversationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConversationController {
    private final ConversationServiceImpl conversationService;

    public ConversationController(ConversationServiceImpl conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(conversationDTO));
    }

    @GetMapping
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        return ResponseEntity.ok(conversationService.getAllConversations());
    }
}