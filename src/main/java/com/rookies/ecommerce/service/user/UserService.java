package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.UserDTO;
import com.rookies.ecommerce.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Service interface for handling user-related operations.
 */
public interface UserService {

    /**
     * Retrieves the information of the currently authenticated user.
     *
     * @return a {@link UserDTO} containing the user's information
     */
    UserDTO getMyInfo();

    /**
     * Updates the profile information of the currently authenticated user.
     *
     * @param info the request object containing updated user information
     */
    void updateProfile(UpdateUserInfo info);

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param request the request object containing the old and new passwords
     */
    void changePassword(ChangePasswordRequest request);

    /**
     * Retrieves the user entity by its unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the {@link User} entity with the specified ID
     */
    User getUserEntityById(UUID id);

    /**
     * Retrieves the user entity associated with the current authentication token.
     *
     * @return the {@link User} entity of the authenticated user
     */
    User getUserFromToken();

}