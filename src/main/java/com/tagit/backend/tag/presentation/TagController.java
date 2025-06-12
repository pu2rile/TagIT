package com.tagit.backend.tag.presentation;

import com.tagit.backend.global.jwt.JwtProvider;
import com.tagit.backend.tag.application.TagService;
import com.tagit.backend.tag.dto.TagInfo;
import com.tagit.backend.tag.dto.TagResponse;
import com.tagit.backend.global.dto.ApiResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<?> createTag(@AuthenticationPrincipal Long userId,
                                       @RequestBody TagInfo tagInfo) {
        System.out.println("현재 유저 ID = " + userId);
        tagService.createTag(userId, tagInfo);
        return ResponseEntity.ok().build();
    }
}