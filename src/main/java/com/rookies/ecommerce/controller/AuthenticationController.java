package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.service.authentication.AuthenticationService;
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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<APIResponse> register(@RequestBody @Valid CreateUserRequest request) {
        authenticationService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new APIResponse(MessageResponse.CREATED_SUCCESSFULLY, null));
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.SUCCESS_REQUEST,
                authenticationService.login(request)));
    }

}
