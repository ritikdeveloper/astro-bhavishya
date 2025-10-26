package com.astro.AstroRitaChaturvedi.Service;

import com.astro.AstroRitaChaturvedi.Model.AstrologerModel;
import com.astro.AstroRitaChaturvedi.Model.CallRequestModel;
import com.astro.AstroRitaChaturvedi.Model.SessionModel;
import com.astro.AstroRitaChaturvedi.Model.UserModel;
import com.astro.AstroRitaChaturvedi.Repository.CallRequestRepository;
import com.astro.AstroRitaChaturvedi.Repository.SessionRepository;
import com.astro.AstroRitaChaturvedi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.RuntimeErrorException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CallService {

    @Autowired
    private CallRequestRepository callRequestRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletService walletService;
    
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public CallRequestModel requestCall(UUID userId, UUID astrologerId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
       double balance = user.getWallet().getBalance();
       if(balance<=50){
           throw new RuntimeException("Low balance please recharge");
       }
       System.out.println("test does not work if service is working");
        AstrologerModel astrologer = new AstrologerModel();
        astrologer.setAstrologerId(astrologerId);

        // Create call request
        CallRequestModel callRequest = new CallRequestModel();
        callRequest.setUser(user);
        callRequest.setAstrologer(astrologer);
        callRequest.setUserPhoneNumber(user.getPhoneNumber());
        callRequest.setStatus(CallRequestModel.CallStatus.REQUESTED);

        CallRequestModel savedRequest = callRequestRepository.save(callRequest);
        
        // Send notification to astrologer
        notificationService.notifyCallRequest(astrologerId, userId, user.getFullName() != null ? user.getFullName() : user.getUsername());
        
        return savedRequest;
    }

    @Transactional
    public CallRequestModel acceptCallRequest(UUID requestId) {
        CallRequestModel callRequest = callRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Call request not found"));

        callRequest.setStatus(CallRequestModel.CallStatus.ACCEPTED);
        callRequest.setCallbackTime(LocalDateTime.now());

        // Create a session for billing
        SessionModel session = new SessionModel();
        session.setUser(callRequest.getUser());
        session.setAstrologer(callRequest.getAstrologer());
        session.setSessionType(SessionModel.SessionType.CALL);
        session.setStatus(SessionModel.SessionStatus.ACTIVE);
        session.setStartTime(LocalDateTime.now());
        // Set the callRequest reference to maintain bidirectional relationship
        session.setCallRequest(callRequest);

        SessionModel savedSession = sessionRepository.save(session);
        callRequest.setSession(savedSession);

        CallRequestModel savedRequest = callRequestRepository.save(callRequest);
        
        // Send notification to user
        notificationService.notifyCallAccepted(callRequest.getUser().getCustomerId(), 
            callRequest.getAstrologer().getUser().getFullName() != null ? 
            callRequest.getAstrologer().getUser().getFullName() : callRequest.getAstrologer().getUser().getUsername());
        
        return savedRequest;
    }

    @Transactional
    public CallRequestModel rejectCallRequest(UUID requestId) {
        CallRequestModel callRequest = callRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Call request not found"));

        callRequest.setStatus(CallRequestModel.CallStatus.REJECTED);
        
        CallRequestModel savedRequest = callRequestRepository.save(callRequest);
        
        // Send notification to user
        notificationService.notifyCallRejected(callRequest.getUser().getCustomerId(), 
            callRequest.getAstrologer().getUser().getFullName() != null ? 
            callRequest.getAstrologer().getUser().getFullName() : callRequest.getAstrologer().getUser().getUsername());
        
        return savedRequest;
    }

    @Transactional
    public CallRequestModel completeCall(UUID requestId, int durationMinutes) {
        CallRequestModel callRequest = callRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Call request not found"));

        callRequest.setStatus(CallRequestModel.CallStatus.COMPLETED);

        // Find the associated session
        List<SessionModel> sessions = sessionRepository.findByUserAndStatus(
                callRequest.getUser(), SessionModel.SessionStatus.ACTIVE);

        if (!sessions.isEmpty()) {
            SessionModel session = sessions.get(0);

            // Set end time
            LocalDateTime endTime = LocalDateTime.now();
            session.setEndTime(endTime);

            // Set duration
            session.setDurationMinutes(durationMinutes);

            // Calculate total amount
            double ratePerMinute = callRequest.getAstrologer().getCallRatePerMinute();
            double totalAmount = ratePerMinute * durationMinutes;
            session.setTotalAmount(totalAmount);

            // Update session status
            session.setStatus(SessionModel.SessionStatus.COMPLETED);

            // Deduct amount from user's wallet
            walletService.deductBalance(
                    session.getUser().getCustomerId(),
                    totalAmount,
                    "Call session with " + session.getAstrologer().getUser().getUsername()
            );

            sessionRepository.save(session);
        }

        return callRequestRepository.save(callRequest);
    }

    public List<CallRequestModel> getUserCallRequests(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return callRequestRepository.findByUser(user);
    }

    public List<CallRequestModel> getAstrologerCallRequests(UUID astrologerId) {
        AstrologerModel astrologer = new AstrologerModel();
        astrologer.setAstrologerId(astrologerId);

        return callRequestRepository.findByAstrologer(astrologer);
    }

    public List<CallRequestModel> getPendingCallRequests(UUID astrologerId) {
        AstrologerModel astrologer = new AstrologerModel();
        astrologer.setAstrologerId(astrologerId);

        return callRequestRepository.findByAstrologerAndStatus(
                astrologer, CallRequestModel.CallStatus.REQUESTED);
    }
}
