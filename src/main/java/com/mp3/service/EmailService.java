package com.mp3.service;

import com.mp3.entity.User;

public interface EmailService {
    void sendEmailVerification(User user, String verificationToken);
    void sendPasswordResetEmail(String email, String resetToken);
    void sendWelcomeEmail(User user);
    void sendRegistrationVerification(String email, String verificationToken);
}
