package com.mp3.repository;

import com.mp3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailVerificationToken(String token);

    void deleteByEmailVerificationExpiresAtBefore(LocalDateTime dateTime);
}
