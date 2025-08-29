package com.mp3.service;

import com.mp3.dto.request.ChangePasswordRequest;
import com.mp3.dto.request.LoginRequest;
import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.LoginResponse;
import com.mp3.dto.response.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);

    UserResponse getProfile(String id);
    UserResponse updateProfile(String id, UserResponse updateRequest);

    void changePassword(String id, ChangePasswordRequest request);
}
