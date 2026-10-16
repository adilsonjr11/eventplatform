package br.com.itau.eventplatform.aplication.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final RedisTemplate<String, String> redis;

    public IdempotencyService(@Qualifier("stringRedisTemplate") RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    public boolean alreadyProcessed(
            String transactionId) {

        return Boolean.TRUE.equals(
                redis.hasKey(transactionId));
    }

    public void markProcessed(
            String transactionId) {

        redis.opsForValue()
                .set(transactionId,
                        "true",
                        Duration.ofHours(24));
    }
}