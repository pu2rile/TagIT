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
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime lastOpenedAt,
        List<TagInfo> tags
) {
    public static NoteResponse from(Note note) {
        List<TagInfo> tagInfos = note.getNoteTags().stream()
                .map(noteTag -> {
                    var tag = noteTag.getTag();
                    return new TagInfo(tag.getTagId(), tag.getName(), tag.getColor());
                })
                .toList();

        return NoteResponse.builder()
                .id(note.getId())
                //.title(note.getTitle())
                .content(note.getContent())
                .pinned(note.isPinned())
                .imageUrl(note.getImgUrl())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .lastOpenedAt(note.getLastOpenedAt())
                .tags(tagInfos)
                .build();
    }
}