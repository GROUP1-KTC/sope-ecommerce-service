package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;
import com.sope.sope_ecommerce_backend.services.LiveStreamService;

import java.util.List;

public class LiveStreamServiceImpl implements LiveStreamService {
    @Override
    public LiveStreamResponse startLive(Long shopSlug, String title, String thumbnail) {
        return null;
    }

    @Override
    public LiveStreamResponse endLive(String shopSlug) {
        return null;
    }

    @Override
    public List<LiveStreamResponse> getActiveLives() {
        return List.of();
    }
}
