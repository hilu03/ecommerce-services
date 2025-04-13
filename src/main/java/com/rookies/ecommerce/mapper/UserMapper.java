package com.rookies.ecommerce.mapper;

import com.rookies.ecommerce.dto.request.CreateUserRequest;
import com.rookies.ecommerce.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(CreateUserRequest info);

}
