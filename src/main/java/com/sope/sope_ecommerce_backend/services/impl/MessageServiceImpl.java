package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.MessageDTO;
import com.sope.sope_ecommerce_backend.entities.ConversationEntity;
import com.sope.sope_ecommerce_backend.entities.MessageEntity;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageServiceImpl(MessageRepository messageRepository, ConversationRepository conversationRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public MessageDTO createMessage(MessageDTO messageDTO) {
        System.out.println("Nhận tin nhắn: content=" + messageDTO.getContent() + ", conversationId=" + messageDTO.getConversationId());
        ConversationEntity conversation = conversationRepository.findById(messageDTO.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        System.out.println("Tìm thấy cuộc hội thoại: id=" + conversation.getId());

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(messageDTO.getContent());
        messageEntity.setSender(messageDTO.getSender());
        messageEntity.setSentAt(LocalDateTime.now());
        messageEntity.setConversation(conversation);
        if (messageDTO.getFile() != null) {
            MessageEntity.FileInfo fileInfo = new MessageEntity.FileInfo(
                    messageDTO.getFile().getUrl(),
                    messageDTO.getFile().getName(),
                    messageDTO.getFile().getType()
            );
            messageEntity.setFile(fileInfo);
        }

        MessageEntity savedMessage = messageRepository.save(messageEntity);
        System.out.println("Đã lưu tin nhắn: id=" + savedMessage.getId());

        MessageDTO result = new MessageDTO();
        result.setId(savedMessage.getId());
        result.setContent(savedMessage.getContent());
        result.setSender(savedMessage.getSender());
        result.setTimestamp(savedMessage.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        result.setConversationId(savedMessage.getConversation().getId());
        if (savedMessage.getFile() != null) {
            result.setFile(new MessageDTO.FileInfo(
                    savedMessage.getFile().getUrl(),
                    savedMessage.getFile().getName(),
                    savedMessage.getFile().getType()
            ));
        }

        System.out.println("Gửi tin nhắn qua WebSocket đến /topic/conversation/" + messageDTO.getConversationId());
        messagingTemplate.convertAndSend("/topic/conversation/" + messageDTO.getConversationId(), result);

        return result;
    }

    public List<MessageDTO> getMessagesByConversationId(String conversationId) {
        return messageRepository.findByConversationId(conversationId).stream()
                .sorted(Comparator.comparing(MessageEntity::getSentAt)).map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setId(message.getId());
            dto.setContent(message.getContent());
            dto.setSender(message.getSender());
            dto.setTimestamp(message.getSentAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            dto.setConversationId(message.getConversation().getId());
            if (message.getFile() != null) {
                dto.setFile(new MessageDTO.FileInfo(
                        message.getFile().getUrl(),
                        message.getFile().getName(),
                        message.getFile().getType()
                ));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}