package com.mp3.service.impl;

import com.mp3.entity.User;
import com.mp3.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendEmailVerification(User user, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Xác thực email - MP3 App");

            String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + verificationToken;
            String emailContent = String.format(
                "Chào %s,\n\n" +
                "Cảm ơn bạn đã đăng ký tài khoản tại MP3 App!\n\n" +
                "Vui lòng nhấp vào liên kết bên dưới để xác thực email của bạn:\n\n" +
                "%s\n\n" +
                "Liên kết này sẽ hết hạn sau 24 giờ.\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ MP3 App",
                user.getDisplayName(),
                verificationUrl
            );

            message.setText(emailContent);
            mailSender.send(message);

            log.info("Email verification sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}, error: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Không thể gửi email xác thực. Vui lòng thử lại.");
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Đặt lại mật khẩu - MP3 App");

            String resetUrl = baseUrl + "/api/auth/reset-password?token=" + resetToken;
            String emailContent = String.format(
                "Chào bạn,\n\n" +
                "Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản MP3 App.\n\n" +
                "Nhấp vào liên kết bên dưới để đặt lại mật khẩu:\n\n" +
                "%s\n\n" +
                "Liên kết này sẽ hết hạn sau 1 giờ.\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ MP3 App",
                resetUrl
            );

            message.setText(emailContent);
            mailSender.send(message);

            log.info("Password reset email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}, error: {}", email, e.getMessage());
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại.");
        }
    }

    @Override
    public void sendWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Chào mừng đến với MP3 App!");

            String emailContent = String.format(
                "Chào %s,\n\n" +
                "Chào mừng bạn đã gia nhập cộng đồng MP3 App!\n\n" +
                "Email của bạn đã được xác thực thành công. Bây giờ bạn có thể:\n" +
                "- Nghe nhạc yêu thích\n" +
                "- Tạo playlist cá nhân\n" +
                "- Khám phá âm nhạc mới\n" +
                "- Kết nối với bạn bè\n\n" +
                "Chúc bạn có những trải nghiệm tuyệt vời!\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ MP3 App",
                user.getDisplayName()
            );

            message.setText(emailContent);
            mailSender.send(message);

            log.info("Welcome email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}, error: {}", user.getEmail(), e.getMessage());
        }
    }

    @Override
    public void sendRegistrationVerification(String email, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Xác thực email để đăng ký - MP3 App");

            String verificationUrl = baseUrl + "/api/auth/verify-registration?token=" + verificationToken;
            String emailContent = String.format(
                "Chào bạn,\n\n" +
                "Cảm ơn bạn đã quan tâm đến MP3 App!\n\n" +
                "Để hoàn tất quá trình đăng ký, vui lòng nhấp vào liên kết bên dưới để xác thực email:\n\n" +
                "%s\n\n" +
                "Sau khi xác thực email, bạn sẽ được chuyển hướng để tạo mật khẩu và hoàn tất đăng ký.\n\n" +
                "Liên kết này sẽ hết hạn sau 30 phút.\n\n" +
                "Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ MP3 App",
                verificationUrl
            );

            message.setText(emailContent);
            mailSender.send(message);

            log.info("Registration verification email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send registration verification email to: {}, error: {}", email, e.getMessage());
            throw new RuntimeException("Không thể gửi email xác thực. Vui lòng thử lại.");
        }
    }
}
