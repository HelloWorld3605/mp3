package com.mp3.controller;


import com.mp3.service.cloudinary.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Tag(name = "Upload File", description = "API cho người dùng upload file (ảnh, nhạc, video) lên Cloudinary")
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Upload file", description = "Upload file (ảnh, nhạc, video) lên Cloudinary và trả về URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload thành công"),
            @ApiResponse(responseCode = "400", description = "File không hợp lệ hoặc lỗi upload"),
            @ApiResponse(responseCode = "409", description = "Xung đột khi upload")
    })
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // Kiểm tra file có rỗng không
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File không được để trống");
            }

            // Kiểm tra kích thước file (ví dụ: tối đa 50MB)
            if (file.getSize() > 50 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("File quá lớn. Kích thước tối đa: 50MB");
            }

            String url = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload error: " + e.getMessage());
        }
    }

}
