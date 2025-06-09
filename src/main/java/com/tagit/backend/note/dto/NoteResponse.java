package com.tagit.backend.note.dto;

import com.tagit.backend.note.domain.entity.Note;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record NoteResponse(
        List<NoteInfo> notes
) {
    public static NoteResponse of(List<NoteInfo> notes) {
        return NoteResponse.builder()
                .notes(notes)
                .build();
    }

    public static NoteInfo convertToNoteInfo(Note note) {
        return new NoteInfo(
                note.getId(),
                note.getTitle(),
                note.getContent(),
                note.isPinned(),
                note.getImgUrl(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getLastOpenedAt()
        );
    }
}