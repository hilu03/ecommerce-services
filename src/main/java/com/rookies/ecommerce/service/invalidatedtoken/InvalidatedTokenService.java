package com.rookies.ecommerce.service.invalidatedtoken;

import java.time.Instant;

/**
 * Service interface for handling invalidated token operations.
 */
public interface InvalidatedTokenService {

    /**
     * Invalidates a token by storing it with its expiration time.
     *
     * @param token the token to be invalidated
     * @param expirationTime the expiration time of the token
     */
    void invalidateToken(String token, Instant expirationTime);

    /**
     * Checks if a token has been invalidated.
     *
     * @param token the token to check
     * @return true if the token is invalidated, false otherwise
     */
    boolean isTokenInvalidated(String token);

}