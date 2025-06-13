package com.tagit.backend.tag.dto;

import com.tagit.backend.tag.domain.entity.Tag;

public record TagInfo(
        Long tagId,
        String name,
        String color
) {
    public static TagInfo from(Tag tag) {
        return new TagInfo(tag.getTagId(), tag.getName(), tag.getColor());
    }
}
