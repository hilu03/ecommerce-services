package com.rookies.ecommerce.service.user;

import com.rookies.ecommerce.dto.response.UserDTO;
import org.springframework.data.domain.Page;

/**
 * Service interface for handling admin-related user operations.
 */
public interface AdminService {

    /**
     * Retrieves a paginated list of users based on their active status.
     *
     * @param isActive the active status of the users (true for active, false for inactive)
     * @param page the page number to retrieve
     * @param size the number of users per page
     * @param sortBy the field to sort the users by
     * @param sortDir the direction of sorting (asc for ascending, desc for descending)
     * @return a paginated list of {@link UserDTO} objects
     */
    Page<UserDTO> getAllUsersByStatus(boolean isActive, int page, int size, String sortBy, String sortDir);

    /**
     * Toggles the active status of a user.
     *
     * @param email the email of the user whose status is to be toggled
     */
    void toggleUserStatus(String email);

}