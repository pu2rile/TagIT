package com.tagit.backend.note.application;

import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.note.domain.repository.NoteRepository;
import com.tagit.backend.note.dto.NoteInfo;
import com.tagit.backend.note.dto.NoteResponse;
import com.tagit.backend.tag.domain.entity.Tag;
import com.tagit.backend.tag.domain.repository.TagRepository;
import com.tagit.backend.tag.dto.TagInfo;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public Page<NoteResponse> getNotesByUser(Long userId, String sort, int page, int size) {
        Sort.Direction direction = sort.equals("old") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
        return noteRepository.findByUserId(userId, pageable)
                .map(NoteResponse::from);
    }

    @Transactional
    public NoteResponse createNote(Long userId, NoteInfo info) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Note note = Note.builder()
                .user(user)
                //.title(info.title())
                .content(info.content())
                .pinned(info.pinned())
                .imgUrl(null)
                .build();

        noteRepository.save(note);

        // 태그 연결
        if (info.tagIds() != null) {
            info.tagIds().forEach(tagId -> {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다."));
                note.addNoteTag(tag);
            });
        }

        List<TagInfo> tagInfos = note.getNoteTags().stream()
                .map(noteTag -> {
                    Tag tag = noteTag.getTag();
                    return new TagInfo(tag.getTagId(), tag.getName(), tag.getColor());
                })
                .toList();

        return new NoteResponse(
                note.getId(),
                //note.getTitle(),
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
