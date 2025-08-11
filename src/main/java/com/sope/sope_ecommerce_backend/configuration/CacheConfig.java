package com.sope.sope_ecommerce_backend.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.api.StatefulConnection;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6379}")
    private int redisPort;

    @Value("${spring.redis.password:}") // Optional password
    private String redisPassword;


    @Value("${spring.cache.redis.default-ttl:600}")
    private long defaultTtl;

    @Value("${spring.cache.redis.cache-ttls.users:300}")
    private long usersTtl;

    @Value("${spring.cache.redis.cache-ttls.products:1800}")
    private long productsTtl;


    @Value("${spring.redis.database:0}")
    private int redisDatabase;

    @Value("${spring.redis.cluster.nodes:}") // Comma-separated if cluster, e.g., host1:port1,host2:port2
    private String clusterNodes;

    @Value("${spring.redis.ssl:false}")
    private boolean useSsl;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = buildLettuceClientConfiguration();

        if (!clusterNodes.isEmpty()) {
            // Cluster mode
            RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
            for (String node : clusterNodes.split(",")) {
                String[] parts = node.split(":");
                clusterConfig.clusterNode(parts[0], Integer.parseInt(parts[1]));
            }
            if (!redisPassword.isEmpty()) {
                clusterConfig.setPassword(redisPassword);
            }
            return new LettuceConnectionFactory(clusterConfig, clientConfig);
        } else {
            // Standalone mode
            RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
            standaloneConfig.setDatabase(redisDatabase);
            if (!redisPassword.isEmpty()) {
                standaloneConfig.setPassword(redisPassword);
            }
            return new LettuceConnectionFactory(standaloneConfig, clientConfig);
        }
    }

    private LettuceClientConfiguration buildLettuceClientConfiguration() {
        // Pool config with specific type to avoid mismatch
        GenericObjectPoolConfig<StatefulConnection<?, ?>> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(100); // Max connections
        poolConfig.setMaxIdle(50);   // Max idle
        poolConfig.setMinIdle(10);   // Min idle
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxWait(Duration.ofSeconds(1));

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder =
                LettucePoolingClientConfiguration.builder()
                        .poolConfig(poolConfig)
                        .commandTimeout(Duration.ofSeconds(2));

        // SSL if enabled - useSsl() does not take arguments, it's a toggle
        if (useSsl) {
            builder.useSsl();
        }

        // Cluster topology refresh if cluster
        if (!clusterNodes.isEmpty()) {
            ClusterTopologyRefreshOptions topologyRefreshOptions = ClusterTopologyRefreshOptions.builder()
                    .enablePeriodicRefresh(Duration.ofMinutes(10))
                    .enableAllAdaptiveRefreshTriggers()
                    .build();
            ClientOptions clientOptions = ClusterClientOptions.builder()
                    .topologyRefreshOptions(topologyRefreshOptions)
                    .build();
            builder.clientOptions(clientOptions);
        }

        return builder.build();
    }

    @Bean
    public RedisCacheConfiguration defaultRedisCacheConfig() {
        // Key serializer
        RedisSerializer<String> keySerializer = new StringRedisSerializer();

        // Value serializer (Jackson) with secure polymorphic typing - use constructor to avoid deprecated setObjectMapper
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        // Secure polymorphic typing with whitelist
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .allowIfSubType("com.sope.sope_ecommerce_backend.model.")
                .allowIfSubType(java.util.List.class)
                .allowIfSubType(java.util.Map.class)
                .build();
        om.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(om, Object.class);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(defaultTtl))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = defaultRedisCacheConfig();

        // Custom TTL for specific caches
        Map<String, RedisCacheConfiguration> initialConfigs = new HashMap<>();
        initialConfigs.put("users", defaultConfig.entryTtl(Duration.ofMinutes(usersTtl)));
        initialConfigs.put("products", defaultConfig.entryTtl(Duration.ofMinutes(productsTtl)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(initialConfigs)
                .transactionAware()
                .build();
    }

    // Improved error handler with basic fallback logging
    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        return new CacheErrorHandler() {
            private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CacheConfig.class);

            @Override
            public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
                log.warn("Redis GET error - cache: {}, key: {}, err: {}", cache != null ? cache.getName() : "null", key, exception.getMessage());
                // Fallback: Query the database or another source
                // Example: if (cache.getName().equals("users")) { return databaseService.getUserById(key); }
            }

            @Override
            public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
                log.warn("Redis PUT error - cache: {}, key: {}, err: {}", cache != null ? cache.getName() : "null", key, exception.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
                log.warn("Redis EVICT error - cache: {}, key: {}, err: {}", cache != null ? cache.getName() : "null", key, exception.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, Cache cache) {
                log.warn("Redis CLEAR error - cache: {}, err: {}", cache != null ? cache.getName() : "null", exception.getMessage());
            }
        };
    }
}