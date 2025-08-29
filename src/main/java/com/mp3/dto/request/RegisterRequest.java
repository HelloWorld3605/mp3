package com.mp3.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request object để đăng ký tài khoản mới")
public class RegisterRequest {

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email là bắt buộc")
    @Schema(description = "Email của người dùng", example = "newuser@example.com")
    private String email;

    @NotBlank(message = "Password là bắt buộc")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    @Schema(description = "Mật khẩu (tối thiểu 6 ký tự)", example = "password123")
    private String password;

    @NotBlank(message = "Display name là bắt buộc")
    @Size(max = 20, message = "Display name không được vượt quá 20 ký tự")
    @Schema(description = "Tên hiển thị (tối đa 20 ký tự)", example = "John Doe")
    private String displayName;
}
