package com.tagit.backend.user.application;

import com.tagit.backend.global.exception.ApiException;
import com.tagit.backend.global.jwt.JwtProvider;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import com.tagit.backend.user.dto.AuthResponse;
import com.tagit.backend.user.dto.LoginInfo;
import com.tagit.backend.user.dto.SignupInfo;
import com.tagit.backend.user.dto.UserResponse;
import com.tagit.backend.user.exception.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserResponse signup(SignupInfo request) {
        if (userRepository.existsByNickname(request.nickname())) {
            throw new ApiException(AuthErrorCode.ALREADY_EXIST_NICKNAME);
        }

        User user = User.builder()
                .nickname(request.nickname())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getNickname());
    }

    public AuthResponse login(LoginInfo request) {
        User user = userRepository.findByNickname(request.nickname())
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new ApiException(AuthErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtProvider.createToken(user.getId(), user.getNickname());
        String refreshToken = jwtProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        UserResponse userResponse = new UserResponse(user.getId(), user.getNickname());
        return new AuthResponse(accessToken, refreshToken, userResponse);
    }

    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }
}