package com.rookies.ecommerce.service;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.LoginRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.entity.*;
import com.rookies.ecommerce.enums.RoleName;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.CartItemRepository;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import com.rookies.ecommerce.service.authentication.AuthenticationServiceImpl;
import com.rookies.ecommerce.service.invalidatedtoken.InvalidatedTokenService;
import com.rookies.ecommerce.service.jwt.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceUnitTests {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Mock
    RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private InvalidatedTokenService invalidatedTokenService;

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Test
    public void registerUser_emailAlreadyExisted_throwException() {
        CreateUserRequest request = CreateUserRequest.builder()
                .email("existing@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        AppException exception = assertThrows(AppException.class, () -> authenticationService.registerUser(request));
        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());

        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper, roleRepository, passwordEncoder);
    }

    @Test
    void registerUser_validData_successfullyRegisters() {
        // GIVEN
        CreateUserRequest request = CreateUserRequest.builder()
                .email("newuser@example.com")
                .password("password123")
                .build();

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .isActive(true)
                .build();

        UserProfile userProfile = new UserProfile();
        Role role = Role.builder().name("ROLE_USER").build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(user);
        when(userMapper.toUserProfile(request)).thenReturn(userProfile);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        authenticationService.registerUser(request);

        // THEN
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(userProfile, user.getUserProfile());
        assertTrue(user.isActive());
        assertNotNull(user.getCustomer());
        assertNotNull(user.getCustomer().getCart());

        verify(userRepository).existsByEmail("newuser@example.com");
        verify(userMapper).toUser(request);
        verify(userMapper).toUserProfile(request);
        verify(passwordEncoder).encode(request.getPassword());
        verify(roleRepository).findByName("ROLE_USER");
        verify(userRepository).save(user);
    }

    @Test
    void login_emailNotFound_throwException() {
        // GIVEN
        LoginRequest request = new LoginRequest("nonexistent@example.com", "password");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // WHEN & THEN
        AppException ex = assertThrows(AppException.class, () -> authenticationService.login(request));
        assertEquals(ErrorCode.LOGIN_FAILED, ex.getErrorCode());
    }

    @Test
    void login_wrongPassword_throwException() {
        // GIVEN
        LoginRequest request = new LoginRequest("user@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // WHEN & THEN
        AppException ex = assertThrows(AppException.class, () -> authenticationService.login(request));
        assertEquals(ErrorCode.LOGIN_FAILED, ex.getErrorCode());
    }

    @Test
    void login_userDisabled_throwException() {
        // GIVEN
        LoginRequest request = new LoginRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new DisabledException("User is disabled"));

        // WHEN & THEN
        AppException ex = assertThrows(AppException.class, () -> authenticationService.login(request));
        assertEquals(ErrorCode.USER_DISABLED, ex.getErrorCode());
    }

    @Test
    void login_validCredentials_returnsLoginResponse() {
        // GIVEN
        LoginRequest request = new LoginRequest("user@example.com", "password");

        User user = new User();
        user.setEmail(request.getEmail());
        user.setRole(Role.builder()
                        .name(RoleName.USER_ROLE.getName())
                .build());
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        user.setUserProfile(profile);

        Cart cart = new Cart();
        Customer customer = Customer.builder().cart(cart).build();
        user.setCustomer(customer);

        LoginResponse mockResponse = new LoginResponse();
        mockResponse.setFirstName("John");
        mockResponse.setLastName("Doe");

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(userMapper.toLoginResponse(user)).thenReturn(mockResponse);
        when(jwtService.generateToken(user)).thenReturn("mock-token");
        when(jwtService.getExpirationTime()).thenReturn(3600L);
        when(cartItemRepository.countByCart(cart)).thenReturn(5L);

        // WHEN
        LoginResponse response = authenticationService.login(request);

        // THEN
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("mock-token", response.getToken());
        assertEquals(3600L, response.getExpiredIn());
        assertEquals(5, response.getCartItemCount());
    }

}
