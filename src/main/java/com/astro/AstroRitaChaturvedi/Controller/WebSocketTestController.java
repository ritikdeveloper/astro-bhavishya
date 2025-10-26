package com.astro.AstroRitaChaturvedi.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketTestController {

    // Simple REST endpoint to test if the controller is accessible
    @GetMapping("/ws-test")
    @ResponseBody
    public Map<String, String> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "WebSocket controller is working");
        return response;
    }

    // Simple WebSocket endpoint for testing
    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Map<String, String> handleTestMessage(Map<String, String> message) {
        Map<String, String> response = new HashMap<>();
        response.put("received", message.get("message"));
        response.put("response", "Echo: " + message.get("message"));
        return response;
    }
}
