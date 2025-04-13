package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.request.CreateUserRequest;

public interface AuthenticationService {

    void registerUser(CreateUserRequest request);

}
