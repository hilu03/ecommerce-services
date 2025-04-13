package com.rookies.ecommerce.service.authentication;

import com.rookies.ecommerce.constant.RoleName;
import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.entity.Cart;
import com.rookies.ecommerce.entity.Role;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.CartItemRepository;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    CartItemRepository cartItemRepository;

    @Override
    public void registerUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER_ROLE);
        user.setRole(role);
        user.setCart(Cart.builder()
                        .user(user)
                .build());
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

        if (!user.isActive()) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

        LoginResponse response = userMapper.toLoginResponse(user);
        response.setCartItemCount(cartItemRepository.countByCart(user.getCart()));

        return response;
    }
}
