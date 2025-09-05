package com.mp3.controller;


import com.mp3.dto.request.SongRequest;
import com.mp3.dto.response.SongResponse;
import com.mp3.service.SongService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
@Tag(name = "Song Management", description = "API để quản lý bài hát")
@SecurityRequirement(name = "bearerAuth")
public class SongController {

    private final SongService songService;

    @PostMapping("/create")
    @Operation(
            summary = "Tạo bài hát mới",
            description = "Tạo một bài hát mới từ URL đã upload. Yêu cầu người dùng đã đăng nhập."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tạo bài hát thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SongResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu đầu vào không hợp lệ hoặc bài hát đã tồn tại",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực - Cần đăng nhập",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền truy cập",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<SongResponse> createSong(
            @Parameter(
                    description = "Thông tin bài hát cần tạo",
                    required = true,
                    schema = @Schema(implementation = SongRequest.class)
            )
            @Valid @RequestBody SongRequest request
    ) {
        SongResponse response = songService.createSong(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Lấy danh sách tất cả bài hát",
            description = "Lấy danh sách tất cả bài hát chưa bị xóa trong hệ thống"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SongResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực - Cần đăng nhập",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<List<SongResponse>> getAllSongs() {
        List<SongResponse> songs = songService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy chi tiết bài hát",
            description = "Lấy thông tin chi tiết của một bài hát theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy thông tin thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SongResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy bài hát",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực - Cần đăng nhập",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<SongResponse> getSongById(
            @Parameter(description = "ID của bài hát", required = true)
            @PathVariable String id
    ) {
        SongResponse song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Cập nhật bài hát",
            description = "Cập nhật thông tin bài hát. Chỉ chủ sở hữu mới có thể cập nhật."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SongResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu đầu vào không hợp lệ hoặc không có quyền",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy bài hát",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực - Cần đăng nhập",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền cập nhật bài hát này",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<SongResponse> updateSong(
            @Parameter(description = "ID của bài hát", required = true)
            @PathVariable String id,
            @Parameter(
                    description = "Thông tin bài hát cần cập nhật",
                    required = true,
                    schema = @Schema(implementation = SongRequest.class)
            )
            @Valid @RequestBody SongRequest request
    ) {
        SongResponse updatedSong = songService.updateSong(id, request);
        return ResponseEntity.ok(updatedSong);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa bài hát",
            description = "Xóa mềm bài hát (soft delete). Chỉ chủ sở hữu mới có thể xóa."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Xóa thành công",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bài hát đã bị xóa hoặc không có quyền",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Không tìm thấy bài hát",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Chưa xác thực - Cần đăng nhập",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Không có quyền xóa bài hát này",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<String> deleteSong(
            @Parameter(description = "ID của bài hát", required = true)
            @PathVariable String id
    ) {
        songService.deleteSong(id);
        return ResponseEntity.ok("Xóa bài hát thành công");
    }
}
