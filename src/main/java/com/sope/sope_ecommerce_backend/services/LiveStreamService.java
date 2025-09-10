package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;

import java.util.List;

public interface LiveStreamService {
    LiveStreamResponse startLive(Long shopSlug, String title, String thumbnail);

    LiveStreamResponse endLive(String shopSlug);

    List<LiveStreamResponse> getActiveLives();
}
