package com.rookies.ecommerce.service.authentication;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.dto.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    void registerUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);

    void logout(HttpServletRequest request);

    RefreshTokenResponse refreshToken(HttpServletRequest request);

}
