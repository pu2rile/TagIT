package com.tagit.backend.tag.domain.repository;

import com.tagit.backend.tag.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByUserId(Long userId);
}
