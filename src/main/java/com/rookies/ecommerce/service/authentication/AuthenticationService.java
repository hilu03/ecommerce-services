package com.rookies.ecommerce.service.authentication;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;

public interface AuthenticationService {

    void registerUser(CreateUserRequest request);

    LoginResponse login(LoginRequest request);

}
