package com.tagit.backend.tag.application;

import com.tagit.backend.tag.domain.entity.Tag;
import com.tagit.backend.tag.domain.repository.TagRepository;
import com.tagit.backend.tag.dto.TagInfo;
import com.tagit.backend.tag.dto.TagResponse;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public void createTag(Long userId, TagInfo request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Tag tag = Tag.builder()
                .name(request.name())
                .color(request.color())
                .user(user)
                .build();

        tagRepository.save(tag);
    }

    public List<TagResponse> getTagsByUser(Long userId) {
        return tagRepository.findByUserId(userId).stream()
                .map(tag -> new TagResponse(tag.getTagId(), tag.getName(), tag.getColor()))
                .toList();
    }
}
