package com.tagit.backend.note.domain.repository;

import com.tagit.backend.note.domain.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findByUserId(Long userId, Pageable pageable);
    Optional<Note> findByIdAndUserId(Long noteId, Long userId);
}