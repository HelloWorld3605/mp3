package com.mp3.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name; // Tên thể loại, ví dụ: Pop, Rock, EDM

    private String description; // Mô tả thêm

    // Một thể loại có thể có nhiều bài hát
    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Song> songs;
}
