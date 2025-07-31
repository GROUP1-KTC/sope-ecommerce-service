package com.sope.sope_ecommerce_backend.seeder.chat;

import com.sope.sope_ecommerce_backend.entities.ConversationEntity;
import com.sope.sope_ecommerce_backend.entities.MessageEntity;
import com.sope.sope_ecommerce_backend.repositories.ConversationRepository;
import com.sope.sope_ecommerce_backend.repositories.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ChatDataSeeder implements CommandLineRunner {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ChatDataSeeder(ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Kiểm tra xem có data chưa
        if (conversationRepository.count() > 0) {
            return;
        }

        // Tạo conversation mẫu
        ConversationEntity conversation = new ConversationEntity();
        conversation.setName("General Conversation");
        conversation.setCreatedAt(LocalDateTime.now());
        ConversationEntity savedConversation = conversationRepository.save(conversation);

        // Tạo message mẫu
        MessageEntity message1 = new MessageEntity();
        message1.setContent("Hello, welcome to the conversation!");
        message1.setSender("User1");
        message1.setSentAt(LocalDateTime.now());
        message1.setConversation(savedConversation);
        messageRepository.save(message1);

        MessageEntity message2 = new MessageEntity();
        message2.setContent("Hi, nice to meet you!");
        message2.setSender("User2");
        message2.setSentAt(LocalDateTime.now().plusMinutes(1));
        message2.setConversation(savedConversation);
        messageRepository.save(message2);
    }
}