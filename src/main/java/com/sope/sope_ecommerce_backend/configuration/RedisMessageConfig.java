package com.sope.sope_ecommerce_backend.configuration;


import com.sope.sope_ecommerce_backend.events.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class RedisMessageConfig {

    private final RedisSubscriber redisSubscriber;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        container.addMessageListener(redisSubscriber, new ChannelTopic("livestream.events"));
        container.addMessageListener(redisSubscriber, new ChannelTopic("chat.events"));
        container.addMessageListener(redisSubscriber, new ChannelTopic("notification.events"));

        return container;
    }
}
