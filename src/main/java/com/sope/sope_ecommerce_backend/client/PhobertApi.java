package com.sope.sope_ecommerce_backend.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "phobertApi", url = "http://localhost:8000/api/v1")
public interface PhobertApi {

    @PostMapping("/embedding/embed")
    Map<String, Object> getEmbedding(@RequestBody Map<String, String> body);
}
