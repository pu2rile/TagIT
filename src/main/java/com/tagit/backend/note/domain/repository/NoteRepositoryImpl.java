package com.tagit.backend.note.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tagit.backend.note.domain.entity.Note;
import com.tagit.backend.note.domain.entity.QNote;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Note> findSortedNotesByUserId(Long userId, Pageable pageable) {
        QNote note = QNote.note;

        List<Note> notes = queryFactory
                .selectFrom(note)
                .where(note.user.id.eq(userId))
                .orderBy(note.pinned.desc(), note.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(note)
                .where(note.user.id.eq(userId))
                .fetchCount();

        return new PageImpl<>(notes, pageable, total);
    }
}
