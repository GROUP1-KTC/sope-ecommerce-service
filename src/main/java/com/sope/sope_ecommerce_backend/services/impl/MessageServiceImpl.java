package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.MessageSendRequest;
import com.sope.sope_ecommerce_backend.dto.response.MessageResponse;
import com.sope.sope_ecommerce_backend.entities.Conversation;
import com.sope.sope_ecommerce_backend.entities.Message;
import com.sope.sope_ecommerce_backend.entities.message_entity.FileMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.ImageMessage;
import com.sope.sope_ecommerce_backend.entities.message_entity.TextMessage;
import com.sope.sope_ecommerce_backend.mapper.MessageMapper;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.MessageRepository;
import com.sope.sope_ecommerce_backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse sendMessage(MessageSendRequest request) {
        Conversation conversation = conversationRepository.findById(request.conversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Message message;
        String type = request.type().toLowerCase();

        switch (type) {
            case "text" -> {
                TextMessage textMsg = new TextMessage();
                textMsg.setContent(request.content());
                message = textMsg;
            }
            case "image" -> {
                ImageMessage imgMsg = new ImageMessage();
                imgMsg.setImageUrl(request.imageUrl());
                imgMsg.setWidth(request.width());
                imgMsg.setHeight(request.height());
                message = imgMsg;
            }
            case "file" -> {
                FileMessage fileMsg = new FileMessage();
                fileMsg.setFileUrl(request.fileUrl());
                fileMsg.setFileName(request.fileName());
                fileMsg.setFileType(request.fileType());
                fileMsg.setFileSize(request.fileSize());
                message = fileMsg;
            }
            default -> throw new IllegalArgumentException("Invalid message type: " + type);
        }

        message.setSender(request.senderId());
        message.setConversation(conversation);

        Message saved = messageRepository.save(message);

        conversation.setLastMessage(saved);
        conversation.setLastSenderId(request.senderId());
        conversationRepository.save(conversation);

        return messageMapper.toDto(saved);
    }


    @Override
    public List<MessageResponse> getMessagesByConversation(String id) {
        UUID conversationId = UUID.fromString(id);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
        return messageRepository.findByConversationOrderBySentAtAsc(conversation)
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Override
    public void deleteMessage(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new RuntimeException("Message not found");
        }
        messageRepository.deleteById(messageId);
    }

    private MessageResponse toResponse(Message msg) {
        String type;
        String content = null, imageUrl = null, fileUrl = null, fileName = null, fileType = null;
        Integer width = null, height = null;
        Long fileSize = null;

        if (msg instanceof TextMessage tm) {
            type = "text";
            content = tm.getContent();
        } else if (msg instanceof ImageMessage im) {
            type = "image";
            imageUrl = im.getImageUrl();
            width = im.getWidth();
            height = im.getHeight();
        } else if (msg instanceof FileMessage fm) {
            type = "file";
            fileUrl = fm.getFileUrl();
            fileName = fm.getFileName();
            fileType = fm.getFileType();
            fileSize = fm.getFileSize();
        } else {
            throw new IllegalStateException("Unknown message type");
        }

        return new MessageResponse(
                msg.getId(),
                msg.getSender(),
                type,
                content,
                imageUrl,
                width,
                height,
                fileUrl,
                fileName,
                fileType,
                fileSize,
                msg.getSentAt().format(DateTimeFormatter.ISO_DATE_TIME)
        );
    }
}
