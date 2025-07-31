package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.response.ConversationDTO;
import com.sope.sope_ecommerce_backend.dto.response.MessageDTO;
import com.sope.sope_ecommerce_backend.services.impl.ConversationServiceImpl;
import com.sope.sope_ecommerce_backend.services.impl.MessageServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {
    private final MessageServiceImpl messageService;
    private final ConversationServiceImpl conversationService;

    public MessageController(MessageServiceImpl messageService, ConversationServiceImpl conversationService) {
        this.messageService = messageService;
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.createMessage(messageDTO));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByConversationId(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversationId(conversationId));
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDTO>> getAllConversations() {
        return ResponseEntity.ok(conversationService.getAllConversations());
    }
}