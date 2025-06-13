package com.tagit.backend.note.dto;

import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.tag.dto.TagInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record NoteResponse(
        Long id,
        //String title,
        String content,
        boolean pinned,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastOpenedAt
) {
    public static NoteResponse from(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                //.title(note.getTitle())
                .content(note.getContent())
                .pinned(note.isPinned())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .lastOpenedAt(note.getLastOpenedAt())
                .build();
    }
}