package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.User;
import com.rookies.ecommerce.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest info);

    UserProfile toUserProfile(CreateUserRequest info);

    @Mapping(source = "role.name", target = "role")
    LoginResponse toLoginResponse(User user);

    @Mapping(source = "userProfile", target = "userProfile")
    @Mapping(source = "role.name", target = "role")
    UserDTO toUserDTO(User user);

    void updateUserInfo(UpdateUserInfo info, @MappingTarget User user);

    void updateUserProfile(UpdateUserInfo info, @MappingTarget UserProfile userProfile);
}
