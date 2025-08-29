package com.mp3.mapper;

import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.UserResponse;
import com.mp3.entity.User;
import com.mp3.enums.UserRole;

import java.time.LocalDateTime;

public class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static User toUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .displayName(request.getDisplayName())
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
