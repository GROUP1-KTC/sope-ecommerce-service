package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.MessageSendRequest;
import com.sope.sope_ecommerce_backend.dto.response.MessageResponse;
import com.sope.sope_ecommerce_backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageSendRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByConversation(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
