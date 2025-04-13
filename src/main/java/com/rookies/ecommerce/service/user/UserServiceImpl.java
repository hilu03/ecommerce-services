package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.constant.RoleName;
import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.Role;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public UserDTO getMyInfo(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return userMapper.toUserDTO(user);
    }

    @Override
    public void updateUser(String id, UpdateUserInfo info) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        userMapper.updateUserInfo(info, user);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> getAllUsers(int page, int size, String sortBy, String sortDir) {
        Role role = roleRepository.findByName(RoleName.USER_ROLE);
        Page<User> users = userRepository.findAllByRole(role, PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)));
        return users.map(userMapper::toUserDTO);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest request) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void toggleUserStatus(String id, boolean status) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        user.setActive(!user.isActive());
        userRepository.save(user);
    }
}
