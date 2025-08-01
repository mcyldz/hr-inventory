package com.mcyldz.hrinventory.security;

import com.mcyldz.hrinventory.dto.request.AuthRequest;
import com.mcyldz.hrinventory.dto.response.AuthResponse;
import com.mcyldz.hrinventory.entity.RefreshToken;
import com.mcyldz.hrinventory.entity.User;
import com.mcyldz.hrinventory.exception.model.BusinessLogicException;
import com.mcyldz.hrinventory.exception.model.ErrorCode;
import com.mcyldz.hrinventory.repository.RefreshTokenRepository;
import com.mcyldz.hrinventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final long refreshExpirationInSeconds;

    public AuthenticationService(UserDetailsServiceImpl userDetailsService, JwtService jwtService, AuthenticationManager authenticationManager, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, @Value("${jwt.refresh_token.expires_in_s}") long refreshExpirationInSeconds) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.refreshExpirationInSeconds = refreshExpirationInSeconds;
    }

    @Transactional
    public AuthResponse login(AuthRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = createRefreshTokenForUser(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken){

        RefreshToken refreshTokenDb = refreshTokenRepository.findByToken(refreshToken).orElseThrow(()->new BusinessLogicException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (refreshTokenDb.getExpiresAt().isBefore(LocalDateTime.now())){
            refreshTokenRepository.delete(refreshTokenDb);
            throw new BusinessLogicException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        refreshTokenRepository.delete(refreshTokenDb);

        User user = refreshTokenDb.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = createRefreshTokenForUser(user);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    private String createRefreshTokenForUser(User user){

        refreshTokenRepository.deleteByUser(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpirationInSeconds));
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }
}