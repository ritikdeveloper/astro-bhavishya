package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.DTO.ChatMessageDTO;
import com.astro.AstroRitaChaturvedi.Model.*;
import com.astro.AstroRitaChaturvedi.Repository.AstrologerRepository;
import com.astro.AstroRitaChaturvedi.Repository.ChatMessageRepository;
import com.astro.AstroRitaChaturvedi.Repository.SessionRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AstrologerRepository astrologerRepository;

    @Autowired
    private WalletService walletService;

    @Transactional
    public SessionModel startChatSession(UUID userId, UUID astrologerModelId) { // Renamed parameter for clarity
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        AstrologerModel astrologer = astrologerRepository.findById(astrologerModelId)
                .orElseThrow(() -> new RuntimeException("Astrologer not found with AstrologerModel ID: " + astrologerModelId));


        SessionModel session = new SessionModel();
        session.setUser(user);
        session.setAstrologer(astrologer);
        session.setSessionType(SessionModel.SessionType.CHAT);
        session.setStatus(SessionModel.SessionStatus.ACTIVE);
        session.setStartTime(LocalDateTime.now());

        return sessionRepository.save(session);
    }

    @Transactional
    public ChatMessageDTO sendMessage(UUID sessionId, UUID senderId, String content) {
        SessionModel session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));

        UserModel sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + senderId));

        UserModel receiver;
        if (session.getUser() == null || session.getAstrologer() == null || session.getAstrologer().getUser() == null) {
            throw new RuntimeException("Session is missing user or astrologer details.");
        }

        if (sender.getCustomerId().equals(session.getUser().getCustomerId())) {
            receiver = session.getAstrologer().getUser();
        } else if (sender.getCustomerId().equals(session.getAstrologer().getUser().getCustomerId())) {
            receiver = session.getUser();
        } else {
            throw new RuntimeException("Sender is not part of this session.");
        }

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setRead(false);
        message.setSession(session);

        ChatMessage savedMessage = chatMessageRepository.save(message);

        if (savedMessage.getSender() != null) {
            savedMessage.getSender().getUsername();
            savedMessage.getSender().getFullName();
        }
        if (savedMessage.getReceiver() != null) {
            savedMessage.getReceiver().getUsername();
            savedMessage.getReceiver().getFullName();
        }

        return new ChatMessageDTO(savedMessage);
    }

    @Transactional
    public SessionModel endChatSession(UUID sessionId) {
        SessionModel session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));

        if (session.getStatus() == SessionModel.SessionStatus.COMPLETED) {
            return session;
        }

        if (session.getUser() == null || session.getAstrologer() == null || session.getAstrologer().getUser() == null) {
            throw new RuntimeException("Session is missing user or astrologer details for ending session.");
        }


        LocalDateTime endTime = LocalDateTime.now();
        session.setEndTime(endTime);

        long durationMinutes = ChronoUnit.MINUTES.between(session.getStartTime(), endTime);
        if (durationMinutes < 1 && session.getStartTime().until(endTime, ChronoUnit.SECONDS) > 0) {
            durationMinutes = 1;
        } else if (durationMinutes <= 0) {
            durationMinutes = 0;
        }
        session.setDurationMinutes((int) durationMinutes);

        double ratePerMinute = session.getAstrologer().getChatRatePerMinute();
        double totalAmount = ratePerMinute * durationMinutes;
        session.setTotalAmount(totalAmount);

        session.setStatus(SessionModel.SessionStatus.COMPLETED);

        if (totalAmount > 0) {
            walletService.deductBalance(
                    session.getUser().getCustomerId(),
                    totalAmount,
                    "Chat session with " + (session.getAstrologer().getUser().getFullName() != null ? session.getAstrologer().getUser().getFullName() : session.getAstrologer().getUser().getUsername())
            );
        }

        return sessionRepository.save(session);
    }

    public List<ChatMessage> getSessionMessages(UUID sessionId) {
        SessionModel session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));
        return chatMessageRepository.findBySessionOrderByTimestampAsc(session);
    }

    public SessionModel getSessionById(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + sessionId));
    }


    public List<SessionModel> getUserSessions(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return sessionRepository.findByUserOrderByStartTimeDesc(user);
    }

    // Modified to accept optional status
    public List<SessionModel> getAstrologerSessions(UUID astrologerUserId, SessionModel.SessionStatus status) {
        UserModel astrologerUser = userRepository.findById(astrologerUserId)
                .orElseThrow(() -> new RuntimeException("Astrologer's user account not found with ID: " + astrologerUserId));

        if (status != null) {
            return sessionRepository.findByAstrologerUserAndStatusOrderByStartTimeDesc(astrologerUser, status);
        } else {
            return sessionRepository.findByAstrologerUserOrderByStartTimeDesc(astrologerUser);
        }
    }

    // Keep the original method for calls that don't specify status (if any are still using it)
    // or refactor existing calls to use the new signature with null for status.
    // For clarity, let's assume all calls will be updated or this is the primary method.
    public List<SessionModel> getAstrologerSessions(UUID astrologerUserId) {
        return getAstrologerSessions(astrologerUserId, null);
    }


}
