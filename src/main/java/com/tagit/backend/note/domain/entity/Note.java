package com.tagit.backend.note.domain.entity;

import com.tagit.backend.noteTag.domain.entity.NoteTag;
import com.tagit.backend.tag.domain.entity.Tag;
import com.tagit.backend.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private String content;

    private boolean pinned;

    private String imgUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastOpenedAt;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NoteTag> noteTags = new HashSet<>();

    @Builder
    public Note(User user, String title, String content, boolean pinned, String imgUrl) {
        this.user = user;
        //this.title = title;
        this.content = content;
        this.pinned = pinned;
        this.imgUrl = imgUrl;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastOpenedAt = LocalDateTime.now();
    }

    public void addNoteTag(Tag tag) {
        NoteTag noteTag = NoteTag.builder()
                .note(this)
                .tag(tag)
                .build();
        this.noteTags.add(noteTag);
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePinned(boolean pinned) {
        this.pinned = pinned;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void clearNoteTags() {
        this.noteTags.clear();
    }
}
