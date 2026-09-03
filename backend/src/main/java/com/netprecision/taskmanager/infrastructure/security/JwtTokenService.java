package com.netprecision.taskmanager.infrastructure.security;

import com.netprecision.taskmanager.application.security.AuthenticationException;
import com.netprecision.taskmanager.domain.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class JwtTokenService {

    private final String secret;
    private final long expirationSeconds;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public JwtTokenService(
            @Value("${app.jwt.secret:dev-secret-change-me}") String secret,
            @Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds,
            ObjectMapper objectMapper,
            UserRepository userRepository
    ) {
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public String generate(Long userId) {
        String header = encode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = encode("{\"sub\":\"" + userId + "\",\"exp\":" + Instant.now().plusSeconds(expirationSeconds).getEpochSecond() + "}");
        return header + "." + payload + "." + sign(header + "." + payload);
    }

    public Long validateAndGetUserId(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new AuthenticationException("Invalid token");
        }

        String content = parts[0] + "." + parts[1];
        if (!sign(content).equals(parts[2])) {
            throw new AuthenticationException("Invalid token");
        }

        try {
            JsonNode payload = objectMapper.readTree(Base64.getUrlDecoder().decode(parts[1]));
            long expiration = payload.get("exp").longValue();
            if (Instant.now().getEpochSecond() > expiration) {
                throw new AuthenticationException("Token expired");
            }

            Long userId = Long.valueOf(payload.get("sub").stringValue());
            userRepository.findById(userId).orElseThrow(() -> new AuthenticationException("Invalid token"));
            return userId;
        } catch (AuthenticationException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new AuthenticationException("Invalid token");
        }
    }

    private String encode(String value) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Could not sign token", exception);
        }
    }
}
