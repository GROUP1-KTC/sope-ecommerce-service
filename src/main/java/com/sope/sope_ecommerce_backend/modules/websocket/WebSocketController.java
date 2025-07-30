package com.sope.sope_ecommerce_backend.modules.websocket;

import com.sope.sope_ecommerce_backend.modules.chat.dto.MessageDTO;
import com.sope.sope_ecommerce_backend.modules.chat.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final MessageService messageService;

    public WebSocketController(MessageService messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/message")
    public void handleMessage(MessageDTO messageDTO) {
        // Lưu tin nhắn và gửi qua WebSocket
        messageService.createMessage(messageDTO);
    }
}