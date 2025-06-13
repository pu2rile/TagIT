package com.tagit.backend.note.dto;

import java.time.LocalDateTime;
import java.util.List;

public record NoteInfo(
        Long id,
        //String title,
        String content,
        boolean pinned,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastOpenedAt,
        List<Long> tagIds
) {}