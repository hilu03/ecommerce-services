package com.rookies.ecommerce.service.user.impl;

import com.rookies.ecommerce.enums.RoleName;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.Role;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.exception.AppException;
import com.rookies.ecommerce.exception.ErrorCode;
import com.rookies.ecommerce.mapper.UserMapper;
import com.rookies.ecommerce.repository.RoleRepository;
import com.rookies.ecommerce.repository.UserRepository;
import com.rookies.ecommerce.service.user.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {

    RoleRepository roleRepository;

    UserRepository userRepository;

    UserMapper userMapper;

    @Override
    public Page<UserDTO> getAllUsersByStatus(boolean isActive, int page, int size, String sortBy, String sortDir) {
        Role role = roleRepository.findByName(RoleName.USER_ROLE.getName());
        Page<User> users = userRepository.findAllByRoleAndIsActive(role, isActive, PageRequest.of(page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)));
        return users.map(userMapper::toUserDTO);
    }

    @Override
    public void toggleUserStatus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        user.setActive(!user.isActive());
        userRepository.save(user);
    }

}
