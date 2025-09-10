package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.StartLiveRequest;
import com.sope.sope_ecommerce_backend.dto.response.LiveEventMessage;
import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;
import com.sope.sope_ecommerce_backend.entities.LiveStream;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.events.RedisPublisher;
import com.sope.sope_ecommerce_backend.mapper.LiveStreamMapper;
import com.sope.sope_ecommerce_backend.repositories.LiveStreamRepository;
import com.sope.sope_ecommerce_backend.services.LiveStreamService;
import com.sope.sope_ecommerce_backend.services.ShopService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LiveStreamServiceImpl implements LiveStreamService {
    private final LiveStreamRepository liveStreamRepository;

    private final ShopService shopService;
    private final RedisPublisher redisPublisher;
    private final SimpMessagingTemplate messagingTemplate;
    private final LiveStreamMapper liveStreamMapper;



    @Override
    @Transactional
    public LiveStreamResponse startLive(StartLiveRequest request) {
        Shop shop = shopService.getShopEntityById(request.shopId());

        LiveStream live = liveStreamRepository.findByShop_Id(shop.getId())
                .orElseGet(() -> {
                    LiveStream l = new LiveStream();
                    l.setShop(shop);
                    return l;
                });

        live.setTitle(request.title());
        live.setDescription(request.description());
        live.setThumbnail(request.thumbnail());
        live.setLive(true);
        live.setStartedAt(Instant.now());
        live = liveStreamRepository.save(live);

        LiveStreamResponse dto = liveStreamMapper.toDto(live);
        // publish local websocket event (for this instance)
        messagingTemplate.convertAndSend("/topic/livestreams", new LiveEventMessage("LIVE_STARTED", dto));
        // publish to Redis so other instances also forward to their websocket clients
        redisPublisher.publish("livestream.events", new LiveEventMessage("LIVE_STARTED", dto));

        return dto;
    }

    @Override
    public LiveStreamResponse endLive(UUID shopId) {
        LiveStream live = liveStreamRepository.findByShop_Id(shopId)
                .orElseThrow(() -> new RuntimeException("Live stream not found"));
        live.setLive(false);
        live.setEndedAt(Instant.now());
        live = liveStreamRepository.save(live);
        LiveStreamResponse dto = liveStreamMapper.toDto(live);
        messagingTemplate.convertAndSend("/topic/livestreams", new LiveEventMessage("LIVE_ENDED", dto));
        redisPublisher.publish("livestream.events", new LiveEventMessage("LIVE_ENDED", dto));
        return dto;
    }

    @Override
    public List<LiveStreamResponse> getActiveLives() {
        return liveStreamRepository.findByIsLiveTrue().stream()
                .map(liveStreamMapper::toDto).collect(Collectors.toList());
    }
}
