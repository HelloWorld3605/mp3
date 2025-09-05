package com.mp3.repository;

import com.mp3.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String>, JpaSpecificationExecutor<Song> {
    boolean existsByUserIdAndUrl(String userId, String url);

    // Tìm tất cả bài hát chưa bị xóa mềm
    @Query("SELECT s FROM Song s WHERE s.deletedAt IS NULL")
    List<Song> findAllActive();

    // Tìm bài hát theo id và chưa bị xóa mềm
    @Query("SELECT s FROM Song s WHERE s.id = :id AND s.deletedAt IS NULL")
    Optional<Song> findActiveById(@Param("id") String id);

    // Tìm bài hát theo id bao gồm cả đã xóa (dùng cho logic xóa)
    @Query("SELECT s FROM Song s WHERE s.id = :id")
    Optional<Song> findByIdIncludingDeleted(@Param("id") String id);
}
