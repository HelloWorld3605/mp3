package com.mp3.service.impl;

import com.mp3.repository.PendingRegistrationRepository;
import com.mp3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupService {

    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;

    @Scheduled(fixedRate = 3600000) // Chạy mỗi giờ (3600000 milliseconds)
    @Transactional
    public void cleanupExpiredTokens() {
        try {
            LocalDateTime now = LocalDateTime.now();

            // Cleanup expired email verification tokens
            userRepository.deleteByEmailVerificationExpiresAtBefore(now);

            // Cleanup expired pending registrations
            pendingRegistrationRepository.deleteByExpiresAtBefore(now);

            log.info("Cleaned up expired tokens and pending registrations at: {}", now);
        } catch (Exception e) {
            log.error("Error during token cleanup: {}", e.getMessage());
        }
    }
}
