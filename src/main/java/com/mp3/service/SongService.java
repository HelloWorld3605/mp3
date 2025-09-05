package com.mp3.service;

import com.mp3.dto.request.SongRequest;
import com.mp3.dto.response.SongResponse;

public interface SongService {
    SongResponse createSong(SongRequest request);

    SongResponse getSongById(String id);

    java.util.List<SongResponse> getAllSongs();

    SongResponse updateSong(String id, SongRequest request);

    void deleteSong(String id);
}
