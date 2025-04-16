package com.rookies.ecommerce.service.invalidatedtoken;

import com.rookies.ecommerce.entity.InvalidatedToken;
import com.rookies.ecommerce.repository.InvalidatedTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenServiceImpl implements InvalidatedTokenService {

    InvalidatedTokenRepository invalidatedTokenRepository;


    @Override
    public void invalidateToken(String token, Instant expirationTime) {
        invalidatedTokenRepository.save(InvalidatedToken.builder()
                        .token(token)
                        .expirationTime(expirationTime)
                .build());
    }

    @Override
    public boolean isTokenInvalidated(String token) {
        return invalidatedTokenRepository.existsByToken(token);
    }
}
