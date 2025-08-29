package com.mp3.service.impl;

import com.mp3.dto.request.LoginRequest;
import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.UserResponse;
import com.mp3.entity.User;
import com.mp3.mapper.UserMapper;
import com.mp3.repository.UserRepository;
import com.mp3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được đăng ký!");
        }
        // map request -> entity
        User user = UserMapper.toUser(request);

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng!");
        }

        return UserMapper.toUserResponse(user);
    }
}
