// ChatController.java
package com.astro.AstroRitaChaturvedi.Controller;

import com.astro.AstroRitaChaturvedi.DTO.ChatMessageDTO;
import com.astro.AstroRitaChaturvedi.DTO.SessionDTO;
import com.astro.AstroRitaChaturvedi.Model.ChatMessage;
import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/start")
    @PreAuthorize("hasRole('USER') or hasRole('ASTROLOGER')")
    public ResponseEntity<SessionDTO> startChatSession(@RequestBody Map<String, String> request) {
        UUID userId = UUID.fromString(request.get("userId"));
        UUID astrologerModelId = UUID.fromString(request.get("astrologerId"));
        SessionModel session = chatService.startChatSession(userId, astrologerModelId);
        return ResponseEntity.ok(new SessionDTO(session));
    }

    @PostMapping("/end/{sessionId}")
    public ResponseEntity<SessionDTO> endChatSession(@PathVariable String sessionId) {
        SessionModel session = chatService.endChatSession(UUID.fromString(sessionId));
        return ResponseEntity.ok(new SessionDTO(session));
    }

    @GetMapping("/messages/{sessionId}")
    public ResponseEntity<List<ChatMessageDTO>> getSessionMessages(@PathVariable String sessionId) {
        List<ChatMessage> messages = chatService.getSessionMessages(UUID.fromString(sessionId));
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/sessions/{sessionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SessionDTO> getSessionDetails(@PathVariable String sessionId) {
        SessionModel session = chatService.getSessionById(UUID.fromString(sessionId));
        return ResponseEntity.ok(new SessionDTO(session));
    }

    @GetMapping("/sessions/user/{userId}")
    public ResponseEntity<List<SessionDTO>> getUserSessions(@PathVariable String userId) {
        List<SessionModel> sessions = chatService.getUserSessions(UUID.fromString(userId));
        List<SessionDTO> sessionDTOs = sessions.stream()
                .map(SessionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessionDTOs);
    }

    @GetMapping("/sessions/astrologer/{astrologerUserId}")
    @PreAuthorize("hasRole('ASTROLOGER') or hasRole('ADMIN')")
    public ResponseEntity<List<SessionDTO>> getAstrologerSessions(@PathVariable String astrologerUserId,
                                                                  @RequestParam(required = false) String status) {
        SessionModel.SessionStatus sessionStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                sessionStatus = SessionModel.SessionStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        List<SessionModel> sessions = chatService.getAstrologerSessions(UUID.fromString(astrologerUserId), sessionStatus);
        List<SessionDTO> sessionDTOs = sessions.stream()
                .map(SessionDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sessionDTOs);
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Map<String, Object> messageRequest, Principal principal) {
        try {
            UUID sessionId = UUID.fromString(messageRequest.get("sessionId").toString());
            UUID senderId = UUID.fromString(messageRequest.get("senderId").toString());
            String content = messageRequest.get("content").toString();

            ChatMessageDTO messageDTO = chatService.sendMessage(sessionId, senderId, content);

            String receiverUsername = messageDTO.getReceiverUsername();
            if (receiverUsername != null) {
                messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/messages", messageDTO);
            }

            String senderUsername = messageDTO.getSenderUsername();
            if (senderUsername != null && !senderUsername.equals(receiverUsername)) {
                messagingTemplate.convertAndSendToUser(senderUsername, "/queue/messages", messageDTO);
            }

            messagingTemplate.convertAndSend("/topic/session/" + messageDTO.getSessionId(), messageDTO);

        } catch (Exception e) {
            e.printStackTrace();
            if (principal != null) {
                messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/errors",
                        Map.of("error", "Error sending message. Please try again."));
            }
        }
    }
}
