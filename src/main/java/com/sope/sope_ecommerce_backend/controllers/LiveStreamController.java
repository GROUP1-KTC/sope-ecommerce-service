package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.ApiResponse;
import com.sope.sope_ecommerce_backend.dto.request.EndLiveRequest;
import com.sope.sope_ecommerce_backend.dto.request.StartLiveRequest;
import com.sope.sope_ecommerce_backend.dto.response.LiveStreamResponse;
import com.sope.sope_ecommerce_backend.services.LiveStreamService;
import com.sope.sope_ecommerce_backend.utils.ApiResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livestream")
@AllArgsConstructor
public class LiveStreamController {

    private final LiveStreamService liveStreamService;

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<LiveStreamResponse>> start(@RequestBody StartLiveRequest req){
        LiveStreamResponse dto = liveStreamService.startLive(req);
        return ApiResponseUtil.success(dto, "Live stream started successfully");
    }

    @PostMapping("/end")
    public ResponseEntity<ApiResponse<LiveStreamResponse>> end(@RequestBody EndLiveRequest req){
        LiveStreamResponse dto = liveStreamService.endLive(req.shopId());
        return ApiResponseUtil.success(dto, "Live stream ended successfully");
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<LiveStreamResponse>>> active(){
        return ApiResponseUtil.success(liveStreamService.getActiveLives(), "Get active live streams successfully");
    }
}
