package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.ChatRequest;
import com.sope.sope_ecommerce_backend.dto.request.MessageSendRequest;
import com.sope.sope_ecommerce_backend.dto.response.ChatAIResponse;
import com.sope.sope_ecommerce_backend.dto.response.MessageResponse;
import com.sope.sope_ecommerce_backend.services.MessageService;
import com.sope.sope_ecommerce_backend.services.impl.GeminiServiceImpl;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations simpMessagingTemplate;
    final private GeminiServiceImpl geminiService;

    @GetMapping("/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getMessagesByConversation(@PathVariable String conversationId) {
        return ResponseEntity.ok(messageService.getMessagesByConversation(conversationId));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @MessageMapping("/chat")
    public void handleWebSocketMessage(MessageSendRequest request) {
        MessageResponse savedMessage = messageService.sendMessage(request);

        simpMessagingTemplate.convertAndSend(
                "/topic/conversation/" + request.conversationId(),
                savedMessage
        );
    }

    @PostMapping("/chatbot")
    public ResponseEntity<ApiResponse<String>> chat(@RequestBody ChatRequest request) {
        try {
            ChatAIResponse response = geminiService.sendMessage(request);

            return ApiResponseUtil.success(response.reply(), "Chat successful");
        } catch (Exception e) {
            return ApiResponseUtil.internalError("Chat failed", List.of(e.getMessage()));
        }
    }

}
