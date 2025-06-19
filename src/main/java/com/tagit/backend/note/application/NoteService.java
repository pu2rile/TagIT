package com.tagit.backend.note.application;

import com.tagit.backend.global.dto.PageInfo;
import com.tagit.backend.global.exception.ApiException;
import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.note.domain.repository.NoteRepository;
import com.tagit.backend.note.dto.NoteDetail;
import com.tagit.backend.note.dto.NoteInfo;
import com.tagit.backend.note.dto.NotePinResponse;
import com.tagit.backend.note.dto.NoteResponse;
import com.tagit.backend.tag.domain.entity.Tag;
import com.tagit.backend.tag.domain.repository.TagRepository;
import com.tagit.backend.tag.dto.TagInfo;
import com.tagit.backend.tag.exception.NoteErrorCode;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public NoteResponse getNotesByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Sort 제거
        Page<Note> notes = noteRepository.findNoteByUserId(userId, pageable);

        List<NoteInfo> noteInfos = notes.stream()
                .map(NoteInfo::from)
                .toList();

        PageInfo pageInfo = PageInfo.of(notes.getNumber(), notes.getTotalPages());

        return NoteResponse.of(noteInfos, pageInfo);
    }

    @Transactional
    public NoteResponse createNote(Long userId, NoteInfo info) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(NoteErrorCode.USER_NOT_FOUND));

        Note note = Note.builder()
                .user(user)
                .content(info.content())
                .pinned(info.pinned())
                .imgUrl(null)
                .build();

        noteRepository.save(note);

        if (info.tagInfos() != null) {
            info.tagInfos().forEach(tagInfo -> {
                Tag tag = tagRepository.findById(tagInfo.tagId())
                        .orElseThrow(() -> new ApiException(NoteErrorCode.TAG_NOT_FOUND));
                note.addNoteTag(tag);
            });
        }

        return NoteResponse.of(
                List.of(NoteInfo.from(note)),
                PageInfo.of(0, 1)
        );
    }

    @Transactional(readOnly = true)
    public NoteDetail getNoteById(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ApiException(NoteErrorCode.NOTE_NOT_FOUND));

        List<TagInfo> tags = note.getNoteTags().stream()
                .map(nt -> TagInfo.from(nt.getTag()))
                .toList();

        return NoteDetail.from(note);
    }

    @Transactional
    public NoteDetail updateNote(Long userId, Long noteId, NoteInfo info) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ApiException(NoteErrorCode.NOTE_NOT_FOUND));

        // note.updateTitle(info.title());
        note.updateContent(info.content());
        note.updatePinned(info.pinned());
        note.updateImgUrl(null);

        // 태그가 전달된 경우에만 기존 태그 삭제 후 새로 연결
        if (info.tagInfos() != null) {
            note.clearNoteTags();

            info.tagInfos().forEach(tagInfo -> {
                Tag tag = tagRepository.findById(tagInfo.tagId())
                        .orElseThrow(() -> new ApiException(NoteErrorCode.TAG_NOT_FOUND));
                note.addNoteTag(tag);
            });
        }

        List<TagInfo> tagInfos = note.getNoteTags().stream()
                .map(nt -> TagInfo.from(nt.getTag()))
                .toList();

        return NoteDetail.builder()
                .id(note.getId())
                //.title(note.getTitle())
                .content(note.getContent())
                .pinned(note.isPinned())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .lastOpenedAt(note.getLastOpenedAt())
                .tags(tagInfos)
                .build();
    }

    @Transactional
    public NotePinResponse updatePinned(Long userId, Long noteId, boolean pinned) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ApiException(NoteErrorCode.NOTE_NOT_FOUND));

        note.updatePinned(pinned);

        return new NotePinResponse(note.getId(), note.isPinned());
    }

    @Transactional
    public void deleteNote(Long userId, Long noteId) {
        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ApiException(NoteErrorCode.NOTE_NOT_FOUND));

        noteRepository.delete(note);
    }
}
