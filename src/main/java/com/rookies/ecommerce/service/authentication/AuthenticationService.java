package com.rookies.ecommerce.service.authentication;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.dto.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Service interface for handling authentication-related operations.
 */
public interface AuthenticationService {

    /**
     * Registers a new user as customer role in the system.
     *
     * @param request the request object containing user registration details
     */
    void registerUser(CreateUserRequest request);

    /**
     * Authenticates a user and generates a login response.
     *
     * @param request the request object containing login credentials
     * @return a {@link LoginResponse} containing authentication details such as tokens
     */
    LoginResponse login(LoginRequest request);

    /**
     * Logs out the currently authenticated user.
     *
     * @param request the HTTP request object to identify the user session
     */
    void logout(HttpServletRequest request);

    /**
     * Refreshes the authentication token for a user.
     *
     * @param request the HTTP request object containing the refresh token
     * @return a {@link RefreshTokenResponse} containing the new authentication token
     */
    RefreshTokenResponse refreshToken(HttpServletRequest request);

}