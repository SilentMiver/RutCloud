package com.example.rutcloud;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
@Transactional
    List<FileEntity> findAllBySessionId(Long session_id);
}

