package com.mp3.service;

import com.mp3.dto.request.LoginRequest;
import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    UserResponse login(LoginRequest request);
}
