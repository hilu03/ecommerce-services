package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.response.UserDTO;
import org.springframework.data.domain.Page;

public interface AdminService {

    Page<UserDTO> getAllUsersByStatus(boolean isActive, int page, int size, String sortBy, String sortDir);

    void toggleUserStatus(String id, boolean status);

}
