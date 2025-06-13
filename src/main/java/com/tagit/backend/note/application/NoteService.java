package com.tagit.backend.note.application;

import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.note.domain.repository.NoteRepository;
import com.tagit.backend.note.dto.NoteInfo;
import com.tagit.backend.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    public Page<NoteResponse> getNotesByUser(Long userId, String sort, int page, int size) {
        Sort.Direction direction = sort.equals("old") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        return noteRepository.findByUserId(userId, pageable)
                .map(NoteResponse::from);
    }

    public List<NoteResponse> getNotesByUser(Long userId) {
        List<Note> notes = noteRepository.findByUserId(userId);

        List<NoteInfo> noteInfos = notes.stream()
                .map(NoteResponse::convertToNoteInfo)
                .collect(Collectors.toList());

        return List.of(NoteResponse.of(noteInfos));
    }
}
