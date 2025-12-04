package com.example.prjbackend.framework;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenService {

    private final Map<String, Long> tokenStore = new ConcurrentHashMap<>();

    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenStore.put(token, userId);
        return token;
    }

    public Long getUserIdByToken(String token) {
        return tokenStore.get(token);
    }

    public void removeToken(String token) {
        tokenStore.remove(token);
    }
}
