package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.dto.response.LoginResponse;
import com.rookies.ecommerce.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest info);

    @Mapping(source = "role.name", target = "role")
    LoginResponse toLoginResponse(User user);
}
