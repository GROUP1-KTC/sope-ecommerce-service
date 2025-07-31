package com.sope.sope_ecommerce_backend.websocket;

import com.sope.sope_ecommerce_backend.dto.response.MessageDTO;
import com.sope.sope_ecommerce_backend.services.impl.MessageServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final MessageServiceImpl messageService;

    public WebSocketController(MessageServiceImpl messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("/message")
    public void handleMessage(MessageDTO messageDTO) {
        // Lưu tin nhắn và gửi qua WebSocket
        messageService.createMessage(messageDTO);
    }
}