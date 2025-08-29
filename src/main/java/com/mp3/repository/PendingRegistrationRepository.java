package com.mp3.repository;

import com.mp3.entity.PendingRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, String> {

    Optional<PendingRegistration> findByEmail(String email);

    Optional<PendingRegistration> findByVerificationToken(String verificationToken);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
