package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.enums.Sentiment;

public interface SentimentService {
    Sentiment getSentiment(String comments);
}
