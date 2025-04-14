package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDTO getMyInfo(String id);

    void updateProfile(String id, UpdateUserInfo info);

    void changePassword(String id, ChangePasswordRequest request);



}
