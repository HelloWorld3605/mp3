package com.mp3.dto.response;

import com.mp3.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object chứa thông tin người dùng")
public class UserResponse {
    @Schema(description = "ID duy nhất của người dùng", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Email của người dùng", example = "user@example.com")
    private String email;

    @Schema(description = "Tên hiển thị của người dùng", example = "John Doe")
    private String displayName;

    @Schema(description = "Vai trò của người dùng trong hệ thống", example = "USER")
    private UserRole role;

    @Schema(description = "URL ảnh đại diện của người dùng", example = "https://example.com/avatar.jpg")
    private String avatarUrl;

    @Schema(description = "Trạng thái xác thực email", example = "true")
    private Boolean isEmailVerified;
}
