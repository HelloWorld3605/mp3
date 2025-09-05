package com.mp3.mapper;

import com.mp3.dto.request.SongRequest;
import com.mp3.dto.response.SongResponse;
import com.mp3.entity.Song;

import java.time.LocalDate;
public class SongMapper {

    private SongMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Map từ SongRequest -> Song entity
    public static Song toEntity(SongRequest request) {
        if (request == null) return null;

        return Song.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .duration(request.getDuration())
                .url(request.getUrl())
                .coverUrl(request.getCoverUrl())
                .releaseDate(
                        request.getReleaseDate() != null
                                ? LocalDate.parse(request.getReleaseDate())
                                : null
                )
                .genre(request.getGenre())
                .build();
    }

    // Map từ Song entity -> SongResponse
    public static SongResponse toResponse(Song song) {
        if (song == null) return null;

        return SongResponse.builder()
                .id(song.getId() != null ? song.getId().toString() : null)
                .title(song.getTitle())
                .description(song.getDescription())
                .duration(song.getDuration())
                .formattedDuration(song.getFormattedDuration())
                .verboseDuration(song.getVerboseDuration())
                .url(song.getUrl())
                .coverUrl(song.getCoverUrl())
                .releaseDate(song.getReleaseDate() != null ? song.getReleaseDate().toString() : null)
                .genre(song.getGenre())
                .playCount(song.getPlayCount())
                .likeCount(song.getLikeCount())
                .createdAt(song.getCreatedAt() != null ? song.getCreatedAt().toString() : null)
                .updatedAt(song.getUpdatedAt() != null ? song.getUpdatedAt().toString() : null)
                .userId(song.getUser() != null && song.getUser().getId() != null ? song.getUser().getId().toString() : null)
                .userName(song.getUser() != null ? song.getUser().getDisplayName() : null)
                .build();
    }
}
