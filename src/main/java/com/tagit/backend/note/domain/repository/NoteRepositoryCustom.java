package com.tagit.backend.note.domain.repository;

import com.tagit.backend.note.domain.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoteRepositoryCustom {
    Page<Note> findSortedNotesByUserId(Long userId, Pageable pageable);
}
