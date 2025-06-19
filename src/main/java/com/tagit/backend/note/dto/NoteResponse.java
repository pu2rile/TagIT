package com.tagit.backend.note.dto;

import com.tagit.backend.global.dto.PageInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record NoteResponse(
        PageInfo pageInfo,
        List<NoteInfo> notes
) {
    public static NoteResponse of(List<NoteInfo> noteInfos, PageInfo pageInfo) {
        return new NoteResponse(pageInfo, noteInfos);
    }
}