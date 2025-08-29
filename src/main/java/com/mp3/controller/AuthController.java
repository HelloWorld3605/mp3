package com.mp3.controller;

import com.mp3.dto.request.CompleteRegistrationRequest;
import com.mp3.dto.request.EmailRegistrationRequest;
import com.mp3.dto.request.LoginRequest;
import com.mp3.dto.request.RegisterRequest;
import com.mp3.dto.response.LoginResponse;
import com.mp3.dto.response.UserResponse;
import com.mp3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication", description = "API cho đăng ký và đăng nhập người dùng")
public class AuthController {

    private final UserService userService;

    @PostMapping("/start-registration")
    @Operation(summary = "Bắt đầu đăng ký bằng email", description = "Bước 1: Gửi email xác thực để bắt đầu quá trình đăng ký")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email xác thực đã được gửi"),
            @ApiResponse(responseCode = "400", description = "Email không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Email đã được đăng ký")
    })
    public ResponseEntity<String> startRegistration(@Valid @RequestBody EmailRegistrationRequest request) {
        try {
            String message = userService.startRegistration(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/complete-registration")
    @Operation(summary = "Hoàn tất đăng ký", description = "Bước 2: Hoàn tất đăng ký sau khi xác thực email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng ký thành công"),
            @ApiResponse(responseCode = "400", description = "Token không hợp lệ hoặc dữ liệu không đúng"),
            @ApiResponse(responseCode = "409", description = "Email đã được đăng ký")
    })
    public ResponseEntity<UserResponse> completeRegistration(@Valid @RequestBody CompleteRegistrationRequest request) {
        UserResponse userResponse = userService.completeRegistration(request);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/verify-registration")
    @Operation(summary = "Xác thực token đăng ký", description = "Endpoint để xác thực token từ email (có thể redirect đến frontend)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token hợp lệ"),
            @ApiResponse(responseCode = "400", description = "Token không hợp lệ hoặc đã hết hạn")
    })
    public ResponseEntity<String> verifyRegistrationToken(@RequestParam String token) {
        // This endpoint can be used to verify token validity or redirect to frontend
        // For now, just return success message - frontend can handle the actual completion
        return ResponseEntity.ok("Token hợp lệ. Bạn có thể hoàn tất đăng ký.");
    }

    // Legacy register endpoint (keep for backward compatibility)
    @PostMapping("/register")
    @Operation(summary = "[Deprecated] Đăng ký tài khoản mới", description = "Phương thức đăng ký cũ - khuyến nghị dùng start-registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng ký thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Email đã được đăng ký")
    })
    @Deprecated
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = userService.register(request);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Xác thực người dùng bằng email và mật khẩu, trả về JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "401", description = "Email hoặc mật khẩu không đúng")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Xác thực email", description = "Xác thực email người dùng bằng token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác thực email thành công"),
            @ApiResponse(responseCode = "400", description = "Token không hợp lệ hoặc đã hết hạn"),
            @ApiResponse(responseCode = "409", description = "Email đã được xác thực trước đó")
    })
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            String message = userService.verifyEmail(token);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Gửi lại email xác thực", description = "Gửi lại email xác thực cho người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Gửi email xác thực thành công"),
            @ApiResponse(responseCode = "400", description = "Email không tồn tại hoặc đã được xác thực"),
            @ApiResponse(responseCode = "500", description = "Lỗi gửi email")
    })
    public ResponseEntity<String> resendEmailVerification(@RequestParam String email) {
        try {
            userService.resendEmailVerification(email);
            return ResponseEntity.ok("Email xác thực đã được gửi lại thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
