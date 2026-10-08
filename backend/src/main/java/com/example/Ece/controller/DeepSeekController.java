package com.example.Ece.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

// Local demonstration only. Do not enable on a public-facing instance without
// authentication, rate limiting, and a deployment-specific origin policy.
@RestController
@RequestMapping("/ai")
@ConditionalOnProperty(name = "deepseek.proxy.enabled", havingValue = "true")
public class DeepSeekController {
    private static final String DEEPSEEK_URL = "https://api.deepseek.com/v1/chat/completions";
    private final RestTemplate client;

    @Value("${deepseek.api.key:}")
    private String apiKey;

    public DeepSeekController() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(45000);
        client = new RestTemplate(factory);
    }

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> chat(@RequestBody JsonNode requestBody, HttpServletRequest request) {
        // This protects a local demo, not a publicly deployed service.
        String remoteAddress = request.getRemoteAddr();
        if (!("127.0.0.1".equals(remoteAddress) || "::1".equals(remoteAddress)
                || "0:0:0:0:0:0:0:1".equals(remoteAddress))) {
            return error(HttpStatus.FORBIDDEN, "Local requests only");
        }
        String origin = request.getHeader("Origin");
        if (origin != null && !("http://localhost:8100".equals(origin)
                || "http://127.0.0.1:8100".equals(origin))) {
            return error(HttpStatus.FORBIDDEN, "Origin is not allowed");
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            return error(HttpStatus.SERVICE_UNAVAILABLE, "DeepSeek is not configured");
        }
        JsonNode messages = requestBody.path("messages");
        if (!messages.isArray() || messages.size() < 1 || messages.size() > 20) {
            return error(HttpStatus.BAD_REQUEST, "Expected 1 to 20 messages");
        }
        int totalLength = 0;
        for (JsonNode message : messages) {
            if (!message.isObject() || !message.path("role").isTextual()
                    || !message.path("content").isTextual()) {
                return error(HttpStatus.BAD_REQUEST, "Invalid message format");
            }
            String role = message.path("role").asText();
            if (!("system".equals(role) || "user".equals(role) || "assistant".equals(role))) {
                return error(HttpStatus.BAD_REQUEST, "Invalid message role");
            }
            totalLength += message.path("content").asText().length();
            if (totalLength > 18000) {
                return error(HttpStatus.BAD_REQUEST, "Messages exceed size limit");
            }
        }

        ObjectNode payload = JsonNodeFactory.instance.objectNode();
        payload.put("model", "deepseek-chat");
        payload.set("messages", messages);
        payload.put("stream", false);
        payload.put("max_tokens", 1024);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        try {
            ResponseEntity<String> upstream = client.exchange(
                    DEEPSEEK_URL, HttpMethod.POST,
                    new HttpEntity<String>(payload.toString(), headers), String.class);
            return ResponseEntity.status(upstream.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(upstream.getBody());
        } catch (RestClientException ex) {
            // Avoid returning upstream response bodies or sensitive diagnostics.
            return error(HttpStatus.BAD_GATEWAY, "DeepSeek request failed");
        }
    }

    private ResponseEntity<String> error(HttpStatus status, String message) {
        ObjectNode errorBody = JsonNodeFactory.instance.objectNode();
        errorBody.put("error", message);
        return ResponseEntity.status(status).contentType(MediaType.APPLICATION_JSON)
                .body(errorBody.toString());
    }
}
