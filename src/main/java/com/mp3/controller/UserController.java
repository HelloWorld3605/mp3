package com.mp3.controller;

import com.mp3.dto.request.ChangePasswordRequest;
import com.mp3.dto.response.UserResponse;
import com.mp3.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý thông tin người dùng")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    // Lấy profile
    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin profile", description = "Lấy thông tin chi tiết của người dùng theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy thông tin thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    })
    public ResponseEntity<UserResponse> getProfile(@PathVariable String id) {
        UserResponse profile = userService.getProfile(id);
        return ResponseEntity.ok(profile);
    }

    // Cập nhật profile
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật profile", description = "Cập nhật thông tin profile của người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    })
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable String id,
            @RequestBody UserResponse updateRequest
    ) {
        UserResponse updatedProfile = userService.updateProfile(id, updateRequest);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/{id}/change-password")
    @Operation(summary = "Đổi mật khẩu", description = "Thay đổi mật khẩu của người dùng")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đổi mật khẩu thành công"),
            @ApiResponse(responseCode = "400", description = "Mật khẩu cũ không đúng hoặc dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy người dùng"),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực")
    })
    public ResponseEntity<String> changePassword(
            @PathVariable String id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        try {
            userService.changePassword(id, request);
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
