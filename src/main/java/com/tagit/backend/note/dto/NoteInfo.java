package com.tagit.backend.note.dto;

import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.tag.dto.TagInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record NoteInfo(
        Long id,
        // String title,
        String content,
        boolean pinned,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastOpenedAt,
        List<TagInfo> tagInfos
) {
    public static NoteInfo from(Note note) {
        List<TagInfo> tagInfos = note.getNoteTags().stream()
                .map(nt -> TagInfo.from(nt.getTag()))
                .toList();

        return new NoteInfo(
                note.getId(),
                // note.getTitle(),
                note.getContent(),
                note.isPinned(),
                note.getImgUrl(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getLastOpenedAt(),
                tagInfos
        );
    }
}