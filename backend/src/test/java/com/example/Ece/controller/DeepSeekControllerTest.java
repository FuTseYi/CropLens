package com.example.Ece.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeepSeekControllerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode messages(String json) throws IOException {
        return mapper.readTree(json);
    }

    private MockHttpServletRequest localRequest(String origin) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/ai/chat");
        request.setRemoteAddr("127.0.0.1");
        if (origin != null) {
            request.addHeader("Origin", origin);
        }
        return request;
    }

    @Test
    void rejectsLanClientsEvenWithAllowedOrigin() throws IOException {
        DeepSeekController controller = new DeepSeekController();
        MockHttpServletRequest request = localRequest("http://localhost:8100");
        request.setRemoteAddr("192.168.1.99");
        assertEquals(HttpStatus.FORBIDDEN, controller.chat(
                messages("{\"messages\":[{\"role\":\"user\",\"content\":\"hello\"}]}"), request
        ).getStatusCode());
    }

    @Test
    void rejectsMissingOrigin() throws IOException {
        DeepSeekController controller = new DeepSeekController();
        assertEquals(HttpStatus.FORBIDDEN, controller.chat(
                messages("{\"messages\":[{\"role\":\"user\",\"content\":\"hello\"}]}"),
                localRequest(null)
        ).getStatusCode());
    }

    @Test
    void rejectsUntrustedBrowserOrigin() throws IOException {
        DeepSeekController controller = new DeepSeekController();
        assertEquals(HttpStatus.FORBIDDEN, controller.chat(
                messages("{\"messages\":[{\"role\":\"user\",\"content\":\"hello\"}]}"),
                localRequest("https://example.org")
        ).getStatusCode());
    }

    @Test
    void rejectsUnconfiguredApiKey() throws IOException {
        DeepSeekController controller = new DeepSeekController();
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, controller.chat(
                messages("{\"messages\":[{\"role\":\"user\",\"content\":\"hello\"}]}"),
                localRequest("http://localhost:8100")
        ).getStatusCode());
    }

    @Test
    void rejectsInvalidMessagesWithoutCallingProvider() throws IOException {
        DeepSeekController controller = new DeepSeekController();
        ReflectionTestUtils.setField(controller, "apiKey", "test-placeholder-only");
        assertEquals(HttpStatus.BAD_REQUEST, controller.chat(
                messages("{\"messages\":[{\"role\":\"invalid-role\",\"content\":\"hello\"}]}"),
                localRequest("http://localhost:8100")
        ).getStatusCode());
    }
}
