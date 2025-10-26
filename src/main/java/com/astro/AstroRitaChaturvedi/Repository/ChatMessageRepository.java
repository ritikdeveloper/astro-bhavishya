package com.astro.AstroRitaChaturvedi.Repository;

import com.astro.AstroRitaChaturvedi.Model.ChatMessage;
import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySession(SessionModel session);
    List<ChatMessage> findBySenderAndReceiver(UserModel sender, UserModel receiver);
    List<ChatMessage> findByReceiverAndReadFalse(UserModel receiver);
    List<ChatMessage> findBySessionOrderByTimestampAsc(SessionModel session); // Added for ordered messages
}
