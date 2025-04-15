package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    UserDTO getMyInfo(String id);

    void updateProfile(String id, UpdateUserInfo info);

    void changePassword(String id, ChangePasswordRequest request);

    User getUserEntityById(UUID id);

}
