package com.tagit.backend.note.dto;

public record NotePinResponse(
        Long noteId,
        boolean pinned
) {}
