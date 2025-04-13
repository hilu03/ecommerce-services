package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDTO getMyInfo(String id);

    void updateUser(String id, UpdateUserInfo info);

    Page<UserDTO> getAllUsers(int page, int size, String sortBy, String sortDir);

    void changePassword(String id, ChangePasswordRequest request);

    void toggleUserStatus(String id, boolean status);

}
