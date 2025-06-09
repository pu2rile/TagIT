package com.tagit.backend.note.application;

import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.note.domain.repository.NoteRepository;
import com.tagit.backend.note.dto.NoteInfo;
import com.tagit.backend.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;

    public List<NoteResponse> getNotesByUser(Long userId) {
        List<Note> notes = noteRepository.findByUserId(userId);

        List<NoteInfo> noteInfos = notes.stream()
                .map(NoteResponse::convertToNoteInfo)
                .collect(Collectors.toList());

        return List.of(NoteResponse.of(noteInfos));
    }
}
