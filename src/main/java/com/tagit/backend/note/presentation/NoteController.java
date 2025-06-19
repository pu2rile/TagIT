package com.tagit.backend.note.presentation;

import com.tagit.backend.global.dto.ApiResponse;
import com.tagit.backend.note.application.NoteService;
import com.tagit.backend.note.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<ApiResponse<NoteResponse>> getNotesByUser(
            @AuthenticationPrincipal Long userId,
            @RequestParam(defaultValue = "recent") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        NoteResponse notes = noteService.getNotesByUser(userId, sort, page, size);
        return ResponseEntity.ok(ApiResponse.success(notes));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(@AuthenticationPrincipal Long userId,
                                                                @RequestBody NoteInfo noteInfo) {
        NoteResponse response = noteService.createNote(userId, noteInfo);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteDetail>> getNoteById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long noteId
    ) {
        NoteDetail response = noteService.getNoteById(userId, noteId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteDetail>> updateNote(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long noteId,
            @RequestBody NoteInfo request
    ) {
        NoteDetail response = noteService.updateNote(userId, noteId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{noteId}/pin")
    public ResponseEntity<ApiResponse<NotePinResponse>> updatePinned(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long noteId,
            @RequestBody NotePinInfo request
    ) {
        NotePinResponse result = noteService.updatePinned(userId, noteId, request.pinned());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long noteId
    ) {
        noteService.deleteNote(userId, noteId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
