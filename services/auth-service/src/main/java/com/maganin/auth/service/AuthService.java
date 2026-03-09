package com.maganin.auth.service;

import com.maganin.auth.domain.RefreshToken;
import com.maganin.auth.domain.User;
import com.maganin.auth.dto.AuthResponse;
import com.maganin.auth.dto.LoginRequest;
import com.maganin.auth.exception.InvalidCredentialsException;
import com.maganin.auth.exception.InvalidTokenException;
import com.maganin.auth.repository.RefreshTokenRepository;
import com.maganin.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh-expiration-days}")
    private Long refreshExpirationDays;

    // valida email/senha, gera access token JWT e refresh token
    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if(user.isInactive()) {
            throw new InvalidCredentialsException();
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtService.generateToken(user);
        String refreshToken = createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getExpirationMinutes() * 60
        );
    }

    // valida o refresh token, revoga o antigo e gera um novo par de tokens
    @Transactional
    public AuthResponse refresh(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(InvalidTokenException::new);

        if (!token.isValid()) {
            throw new InvalidTokenException();
        }

        String newAccessToken = jwtService.generateToken(token.getUser());
        String newRefreshToken = createRefreshToken(token.getUser());

        token.setRevoked(true);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer",
                jwtService.getExpirationMinutes() * 60
        );
    }

    // revoga o refresh token sem precisar de senha
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> token.setRevoked(true));
    }

    // revoga todos os tokens anteriores do usuário antes de criar um novo (garante sessão única)
    private String createRefreshToken(User user) {
        refreshTokenRepository.revokeAllByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(refreshExpirationDays))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
