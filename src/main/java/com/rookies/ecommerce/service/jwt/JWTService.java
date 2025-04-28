package com.rookies.ecommerce.service.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Service interface for handling JWT (JSON Web Token) operations.
 */
public interface JWTService {

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    String extractUsername(String token);

    /**
     * Extracts a specific claim from the given JWT token using a claims resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver a function to resolve the desired claim from the token
     * @param <T> the type of the claim to extract
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    String generateToken(UserDetails userDetails);

    /**
     * Generates a JWT token with additional claims for the given user details.
     *
     * @param extraClaims a map of additional claims to include in the token
     * @param userDetails the user details to include in the token
     * @return the generated JWT token
     */
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    /**
     * Retrieves the expiration time for the JWT tokens.
     *
     * @return the expiration time in milliseconds
     */
    long getExpirationTime();

    /**
     * Validates the given JWT token against the provided user details.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to validate the token against
     * @return true if the token is valid, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    Date extractExpiration(String token);

    /**
     * Extracts the issued-at date from the given JWT token.
     *
     * @param token the JWT token
     * @return the issued-at date of the token
     */
    Date extractIssuedAt(String token);

}