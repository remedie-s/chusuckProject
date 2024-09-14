package com.example.chusuk.demo.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.RefreshToken;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    private final SUserService sUserService;

    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(String refreshToken, int hour) {
        if (!tokenProvider.isVaildToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }
        Integer suserId = findByRefreshToken(refreshToken).getSuserid();
        SUser suser = sUserService.findById(suserId);
        return tokenProvider.generateToken(suser, Duration.ofHours(hour));
    }

    public RefreshToken findByRefreshToken(String refreshToken) {
        Optional<RefreshToken> opt = this.refreshTokenRepository.findByRefreshToken(refreshToken);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public RefreshToken findByUserId(Integer SUserid) {
        Optional<RefreshToken> opt = this.refreshTokenRepository.findById(SUserid);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public void saveRefreshToken(Integer id, String token) {
        System.out.println("Saving refresh token: sUserId=" + id + ", token=" + token);
        RefreshToken refreshToken = new RefreshToken(id, token);
        this.refreshTokenRepository.save(refreshToken);
    }

}
