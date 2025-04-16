package com.rookies.ecommerce.service.authentication;

import com.rookies.ecommerce.dto.response.RefreshTokenResponse;
import com.rookies.ecommerce.enums.RoleName;
import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.entity.*;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.CartItemRepository;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import com.rookies.ecommerce.service.invalidatedtoken.InvalidatedTokenService;
import com.rookies.ecommerce.service.jwt.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    CartItemRepository cartItemRepository;

    AuthenticationManager authenticationManager;

    JWTService jwtService;

    InvalidatedTokenService invalidatedTokenService;

    @Value("${security.jwt.refresh-duration}")
    @NonFinal
    long refreshDuration;

    @Override
    public void registerUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        UserProfile userProfile = userMapper.toUserProfile(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(RoleName.USER_ROLE.getName());
        user.setRole(role);
        user.setUserProfile(userProfile);
        Cart cart = new Cart();
        Customer customer = Customer.builder()
                .user(user)
                .cart(cart)
                .build();
        cart.setCustomer(customer);
        user.setCustomer(customer);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAILED));

            LoginResponse response = userMapper.toLoginResponse(user);
            UserProfile userProfile = user.getUserProfile();
            response.setFirstName(userProfile.getFirstName());
            response.setLastName(userProfile.getLastName());
            response.setToken(jwtService.generateToken(user));
            response.setExpiredIn(jwtService.getExpirationTime());
            if (user.getRole().getName().equals(RoleName.USER_ROLE.getName())) {
                response.setCartItemCount(cartItemRepository.countByCart(user.getCustomer().getCart()));
            }

            return response;
        }
        catch (DisabledException ex) {
            throw new AppException(ErrorCode.USER_DISABLED);
        }
        catch (BadCredentialsException ex) {
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }

    }

    @Override
    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String token = authHeader.substring(7);
        Instant expirationTime = jwtService.extractExpiration(token).toInstant();

        invalidatedTokenService.invalidateToken(token, expirationTime);
    }

    @Override
    public RefreshTokenResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String token = authHeader.substring(7);
        Instant expirationTime = jwtService.extractExpiration(token).toInstant();
        invalidatedTokenService.invalidateToken(token, expirationTime);

        Instant issuedAt = jwtService.extractIssuedAt(token).toInstant();

        if (Instant.now().isAfter(issuedAt.plusMillis(refreshDuration))) {
            throw new AppException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmailAndIsActive(email, true)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_REQUEST));


        return RefreshTokenResponse.builder()
                .token(jwtService.generateToken(user))
                .expiredIn(jwtService.getExpirationTime())
                .build();

    }
}
