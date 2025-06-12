package com.tagit.backend.tag.dto;

public record TagResponse(
        Long tagId,
        String name,
        String color
) {}
