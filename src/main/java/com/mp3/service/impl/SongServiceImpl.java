package com.mp3.service.impl;

import com.mp3.dto.request.SongRequest;
import com.mp3.dto.response.SongResponse;
import com.mp3.entity.Song;
import com.mp3.entity.User;
import com.mp3.exception.ResourceNotFoundException;
import com.mp3.mapper.SongMapper;
import com.mp3.repository.SongRepository;
import com.mp3.service.SongService;
import com.mp3.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {
    private final SongRepository songRepository;
    private final SecurityUtils securityUtils;

    @Override
    public SongResponse createSong(SongRequest request) {
        // Lấy user hiện tại
        User currentUser = securityUtils.getCurrentUserOrThrow();

        // Check nếu user đã upload bài hát với cùng url
        boolean exists = songRepository.existsByUserIdAndUrl(currentUser.getId(), request.getUrl());
        if (exists) {
            throw new IllegalArgumentException("Bạn đã upload bài hát này rồi!");
        }

        // Map request -> entity
        Song song = SongMapper.toEntity(request);
        song.setUser(currentUser);

        // Các giá trị mặc định
        song.setPlayCount(0L);
        song.setLikeCount(0L);

        Song saved = songRepository.save(song);

        // Map entity -> response
        return SongMapper.toResponse(saved);
    }

    @Override
    public SongResponse getSongById(String id) {
        Song song = songRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát!"));
        return SongMapper.toResponse(song);
    }

    @Override
    public List<SongResponse> getAllSongs() {
        return songRepository.findAllActive().stream()
                .map(SongMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SongResponse updateSong(String id, SongRequest request) {
        Song song = songRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát!"));

        User currentUser = securityUtils.getCurrentUserOrThrow();
        if (!song.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền sửa bài hát này!");
        }

        // Cập nhật các trường
        song.setTitle(request.getTitle());
        song.setDescription(request.getDescription());
        song.setDuration(request.getDuration());
        song.setUrl(request.getUrl());
        song.setCoverUrl(request.getCoverUrl());
        song.setReleaseDate(request.getReleaseDate() != null ?
            java.time.LocalDate.parse(request.getReleaseDate()) : null);
        song.setGenre(request.getGenre());

        Song updated = songRepository.save(song);
        return SongMapper.toResponse(updated);
    }

    @Override
    public void deleteSong(String id) {
        Song song = songRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài hát!"));

        // Kiểm tra nếu bài hát đã bị xóa mềm
        if (song.getDeletedAt() != null) {
            throw new IllegalArgumentException("Bài hát này đã bị xóa rồi!");
        }

        User currentUser = securityUtils.getCurrentUserOrThrow();
        if (!song.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa bài hát này!");
        }

        // Xóa mềm: set deletedAt
        song.setDeletedAt(LocalDateTime.now());
        songRepository.save(song);
    }
}
