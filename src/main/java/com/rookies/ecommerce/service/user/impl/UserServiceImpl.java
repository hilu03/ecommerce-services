package com.rookies.ecommerce.service.user.impl;

import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import com.rookies.ecommerce.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getMyInfo() {
        User user = getUserFromToken();

        return userMapper.toUserDTO(user);
    }

    @Override
    public void updateProfile(UpdateUserInfo info) {
        User user = getUserFromToken();

        userMapper.updateUserInfo(info, user);
        userMapper.updateUserProfile(info, user.getUserProfile());
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        User user = getUserFromToken();

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    public User getUserFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED_REQUEST);
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED_REQUEST));
    }

}
