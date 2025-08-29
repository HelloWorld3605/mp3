package com.mp3.service.impl;

import com.mp3.dto.request.ChangePasswordRequest;
import com.mp3.dto.request.CompleteRegistrationRequest;
import com.mp3.dto.request.EmailRegistrationRequest;
import com.mp3.dto.request.LoginRequest;
import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.LoginResponse;
import com.mp3.dto.response.UserResponse;
import com.mp3.entity.PendingRegistration;
import com.mp3.entity.User;
import com.mp3.mapper.UserMapper;
import com.mp3.repository.PendingRegistrationRepository;
import com.mp3.repository.UserRepository;
import com.mp3.service.EmailService;
import com.mp3.service.UserService;
import com.mp3.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PendingRegistrationRepository pendingRegistrationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Override
    public UserResponse register(RegisterRequest request) {
        // check email đã tồn tại
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký!");
        }

        // map request -> entity
        User user = UserMapper.toUser(request);

        // mã hoá password trước khi lưu
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // Generate email verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        user.setEmailVerificationExpiresAt(LocalDateTime.now().plusHours(24));
        user.setIsEmailVerified(false);

        User savedUser = userRepository.save(user);

        // Send email verification
        try {
            emailService.sendEmailVerification(savedUser, verificationToken);
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send verification email: " + e.getMessage());
        }

        // convert User entity to UserResponse
        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng!");
        }

        // Generate JWT token
        String accessToken = jwtUtil.generateToken(user.getEmail(), user.getId());

        // Create login response
        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(jwtExpiration)
                .user(UserMapper.toUserResponse(user))
                .build();
    }

    @Override
    public UserResponse getProfile(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));
        return UserMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateProfile(String id, UserResponse updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // Update only allowed fields
        if (updateRequest.getDisplayName() != null) {
            user.setDisplayName(updateRequest.getDisplayName());
        }
        if (updateRequest.getAvatarUrl() != null) {
            user.setAvatarUrl(updateRequest.getAvatarUrl());
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.toUserResponse(updatedUser);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

        // Verify old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }

        // Update password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public String verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Token xác thực không hợp lệ hoặc đã hết hạn!"));

        // Check if token is expired
        if (user.getEmailVerificationExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token xác thực đã hết hạn!");
        }

        // Check if email is already verified
        if (user.getIsEmailVerified()) {
            throw new RuntimeException("Email đã được xác thực trước đó!");
        }

        // Verify email
        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiresAt(null);

        userRepository.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return "Email đã được xác thực thành công!";
    }

    @Override
    public void resendEmailVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // Check if email is already verified
        if (user.getIsEmailVerified()) {
            throw new RuntimeException("Email đã được xác thực!");
        }

        // Generate new verification token
        String newVerificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(newVerificationToken);
        user.setEmailVerificationExpiresAt(LocalDateTime.now().plusHours(24));

        userRepository.save(user);

        // Send new verification email
        emailService.sendEmailVerification(user, newVerificationToken);
    }

    @Override
    @Transactional
    public String startRegistration(EmailRegistrationRequest request) {
        String email = request.getEmail();

        // Check if email is already registered
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được đăng ký trong hệ thống!");
        }

        // Delete any existing pending registration for this email
        if (pendingRegistrationRepository.existsByEmail(email)) {
            pendingRegistrationRepository.deleteByEmail(email);
        }

        // Generate verification token
        String verificationToken = UUID.randomUUID().toString();

        // Create pending registration
        PendingRegistration pendingRegistration = PendingRegistration.builder()
                .email(email)
                .verificationToken(verificationToken)
                .expiresAt(LocalDateTime.now().plusMinutes(30)) // 30 minutes expiration
                .build();

        pendingRegistrationRepository.save(pendingRegistration);

        // Send verification email
        emailService.sendRegistrationVerification(email, verificationToken);

        return "Email xác thực đã được gửi đến " + email + ". Vui lòng kiểm tra hộp thư và làm theo hướng dẫn để hoàn tất đăng ký.";
    }

    @Override
    @Transactional
    public UserResponse completeRegistration(CompleteRegistrationRequest request) {
        // Find pending registration by token
        PendingRegistration pendingRegistration = pendingRegistrationRepository
                .findByVerificationToken(request.getVerificationToken())
                .orElseThrow(() -> new RuntimeException("Token xác thực không hợp lệ hoặc đã hết hạn!"));

        // Check if token is expired
        if (pendingRegistration.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token xác thực đã hết hạn! Vui lòng thực hiện đăng ký lại.");
        }

        // Check if email is already registered (double check)
        if (userRepository.existsByEmail(pendingRegistration.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký trong hệ thống!");
        }

        // Create user account
        User user = User.builder()
                .email(pendingRegistration.getEmail())
                .displayName(request.getDisplayName())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .isEmailVerified(true) // Email is already verified
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Delete pending registration
        pendingRegistrationRepository.delete(pendingRegistration);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return UserMapper.toUserResponse(savedUser);
    }
}
