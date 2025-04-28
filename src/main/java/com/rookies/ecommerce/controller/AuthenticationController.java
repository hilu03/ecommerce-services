package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling user authentication and authorization operations.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
public class AuthenticationController {

    AuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param request the request object containing user registration details
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the registration status
     */
    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody @Valid CreateUserRequest request) {
        authenticationService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    /**
     * Authenticates a user and generates a login token.
     *
     * @param request the request object containing login credentials
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the login token
     */
    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST,
                authenticationService.login(request)));
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @param request the HTTP request containing authentication details
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the logout status
     */
    @PostMapping("/logout")
    public ResponseEntity<APIResponse> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST, null));
    }

    /**
     * Refreshes the authentication token for the currently authenticated user.
     *
     * @param request the HTTP request containing the current token
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the refreshed token
     */
    @PostMapping("/refresh")
    public ResponseEntity<APIResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST,
                authenticationService.refreshToken(request)));
    }

}