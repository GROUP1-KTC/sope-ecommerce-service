package com.sope.sope_ecommerce_backend.modules.chat.seeder;

import com.sope.sope_ecommerce_backend.modules.chat.entity.Conversation;
import com.sope.sope_ecommerce_backend.modules.chat.entity.Message;
import com.sope.sope_ecommerce_backend.modules.chat.repository.ConversationRepository;
import com.sope.sope_ecommerce_backend.modules.chat.repository.MessageRepository;
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
        Conversation conversation = new Conversation();
        conversation.setName("General Conversation");
        conversation.setCreatedAt(LocalDateTime.now());
        Conversation savedConversation = conversationRepository.save(conversation);

        // Tạo message mẫu
        Message message1 = new Message();
        message1.setContent("Hello, welcome to the conversation!");
        message1.setSender("User1");
        message1.setSentAt(LocalDateTime.now());
        message1.setConversation(savedConversation);
        messageRepository.save(message1);

        Message message2 = new Message();
        message2.setContent("Hi, nice to meet you!");
        message2.setSender("User2");
        message2.setSentAt(LocalDateTime.now().plusMinutes(1));
        message2.setConversation(savedConversation);
        messageRepository.save(message2);
    }
}