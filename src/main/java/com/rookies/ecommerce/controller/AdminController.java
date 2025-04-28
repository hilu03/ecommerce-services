package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.user.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling admin-related user management activities.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
@Tag(name = "Admin", description = "APIs related to user management activities for admin")
public class AdminController {

    AdminService adminService;

    /**
     * Retrieves a paginated list of all active users.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of users per page (default is 10)
     * @param sortBy the field to sort the users by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of active users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<APIResponse> getAllActiveUsers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "createdAt") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                adminService.getAllUsersByStatus(true, page, size, sortBy, sortDir)));
    }

    /**
     * Retrieves a paginated list of all inactive users.
     *
     * @param page the page number to retrieve (default is 0)
     * @param size the number of users per page (default is 10)
     * @param sortBy the field to sort the users by (default is "createdAt")
     * @param sortDir the direction of sorting (asc for ascending, desc for descending; default is "asc")
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the list of inactive users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/inactive")
    public ResponseEntity<APIResponse> getAllInactiveUsers(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                adminService.getAllUsersByStatus(false, page, size, sortBy, sortDir)));
    }

    /**
     * Toggles the active status of a user.
     *
     * @param id the unique identifier of the user
     * @param status the new status to set for the user (true for active, false for inactive)
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/toggle-status")
    public ResponseEntity<APIResponse> toggleUserStatus(@RequestParam String id,
                                                        @RequestParam boolean status) {
        adminService.toggleUserStatus(id, status);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}