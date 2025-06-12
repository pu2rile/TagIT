package com.tagit.backend.user.application;

import com.tagit.backend.global.jwt.JwtProvider;
import com.tagit.backend.user.domain.entity.User;
import com.tagit.backend.user.domain.repository.UserRepository;
import com.tagit.backend.user.dto.AuthResponse;
import com.tagit.backend.user.dto.LoginInfo;
import com.tagit.backend.user.dto.SignupInfo;
import com.tagit.backend.user.dto.UserResponse;
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
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
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
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        String token = jwtProvider.createToken(user.getId(), user.getNickname());
        UserResponse userResponse = new UserResponse(user.getId(), user.getNickname());

        return new AuthResponse(token, userResponse);
    }
}