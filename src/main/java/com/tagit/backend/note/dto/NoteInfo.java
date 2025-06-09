package com.tagit.backend.note.dto;

import java.time.LocalDateTime;

public record NoteInfo(
        Long id,
        String title,
        String content,
        boolean pinned,
        String imageUrl,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        LocalDateTime lastOpenedAt
) {}