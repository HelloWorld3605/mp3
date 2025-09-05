package com.mp3.repository;

import com.mp3.entity.FileResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileResourceRepository extends JpaRepository<FileResource, String> {
    Optional<FileResource> findByHash(String hash);
}
