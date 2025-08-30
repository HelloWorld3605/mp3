package com.mp3.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String description;

    // Thể loại (có thể null)
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // Thời lượng (giây)
    @Column(nullable = false)
    private Integer duration;

    // Link file nhạc (bắt buộc)
    @Column(nullable = false)
    private String url;

    // Ảnh bìa (có thể null)
    private String coverUrl;

    private LocalDate releaseDate;

    @Column(nullable = false)
    private Long playCount = 0L;

    @Column(nullable = false)
    private Long likeCount = 0L;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Transient
    public String getFormattedDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Transient
    public String getVerboseDuration() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d phút %d giây", minutes, seconds);
    }

}
