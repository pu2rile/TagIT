package com.tagit.backend.user.presentation;

import com.tagit.backend.global.dto.ApiResponse;
import com.tagit.backend.user.application.UserService;
import com.tagit.backend.user.dto.AuthResponse;
import com.tagit.backend.user.dto.LoginInfo;
import com.tagit.backend.user.dto.SignupInfo;
import com.tagit.backend.user.dto.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody SignupInfo request) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginInfo request,
                                                           HttpServletResponse response) {
        AuthResponse authResponse = userService.login(request);

        Cookie cookie = new Cookie("token", authResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1일
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponse.success(authResponse.user()));
    }
}
