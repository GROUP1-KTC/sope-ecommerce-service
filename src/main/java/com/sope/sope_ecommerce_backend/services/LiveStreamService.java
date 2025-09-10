package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.dto.request.StartLiveRequest;
import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;

import java.util.List;
import java.util.UUID;

public interface LiveStreamService {
    LiveStreamResponse startLive(StartLiveRequest req);

    LiveStreamResponse endLive(UUID shopId);

    List<LiveStreamResponse> getActiveLives();
}
