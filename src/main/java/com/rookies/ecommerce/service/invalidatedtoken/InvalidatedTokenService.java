package com.rookies.ecommerce.service.invalidatedtoken;

import java.time.Instant;

public interface InvalidatedTokenService {

    void invalidateToken(String token, Instant expirationTime);

    boolean isTokenInvalidated(String token);

}
