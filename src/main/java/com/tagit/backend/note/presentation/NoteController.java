package com.tagit.backend.note.presentation;

import com.tagit.backend.global.dto.ApiResponse;
import com.tagit.backend.note.application.NoteService;
import com.tagit.backend.note.dto.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ApiResponse<List<NoteResponse>> getNotesByUser(@RequestParam Long userId) {
        List<NoteResponse> noteResponses = noteService.getNotesByUser(userId);
        return ApiResponse.success(noteResponses);
    }
}
