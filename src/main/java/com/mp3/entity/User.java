package com.mp3.entity;

import com.mp3.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "avatar_url")
    private String avatarUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = UserRole.USER;
        }
        if (this.avatarUrl == null || this.avatarUrl.isEmpty()) {
            this.avatarUrl = "https://res.cloudinary.com/dz9q8zkeh/image/upload/v1756438809/istockphoto-1393750072-612x612_n1h47x.jpg";
        }
        if (this.displayName == null || this.displayName.isEmpty()) {
            this.displayName = this.email.substring(0, this.email.indexOf("@"));
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}


