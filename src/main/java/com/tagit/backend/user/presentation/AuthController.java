package com.tagit.backend.user.presentation;

import com.tagit.backend.global.dto.ApiResponse;
import com.tagit.backend.global.exception.ApiException;
import com.tagit.backend.global.jwt.JwtProvider;
import com.tagit.backend.user.application.UserService;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import com.tagit.backend.user.dto.AuthResponse;
import com.tagit.backend.user.dto.LoginInfo;
import com.tagit.backend.user.dto.SignupInfo;
import com.tagit.backend.user.dto.UserResponse;
import com.tagit.backend.user.exception.AuthErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signup(@RequestBody SignupInfo request) {
        UserResponse response = userService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginInfo request,
                                                           HttpServletResponse response) {
        AuthResponse authResponse = userService.login(request);

        // 엑세스 토큰 쿠키
        Cookie accessTokenCookie = new Cookie("token", authResponse.accessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(15 * 60); // 15분

        // 리프레시 토큰 쿠키
        Cookie refreshTokenCookie = new Cookie("refreshToken", authResponse.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(ApiResponse.success(authResponse.user()));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue("refreshToken") String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new ApiException(AuthErrorCode.INVALID_TOKEN);
        }

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtProvider.createToken(user.getId(), user.getNickname());

        return ResponseEntity.ok(newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        user.clearRefreshToken();
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
