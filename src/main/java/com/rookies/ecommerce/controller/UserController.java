package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing user-related activities.
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Tag(name = "Users", description = "APIs related to user activities")
public class UserController {

    UserService userService;

    /**
     * Retrieves the information of the currently authenticated user.
     *
     * @return a {@link ResponseEntity} containing an {@link APIResponse} with the user's information
     */
    @GetMapping("/me")
    public ResponseEntity<APIResponse> getMyInfo() {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                userService.getMyInfo()));
    }

    /**
     * Updates the profile information of the currently authenticated user.
     *
     * @param info the request object containing updated user information
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the update status
     */
    @PutMapping("/me")
    public ResponseEntity<APIResponse> updateUser(@RequestBody UpdateUserInfo info) {
        userService.updateProfile(info);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    /**
     * Changes the password of the currently authenticated user.
     *
     * @param request the request object containing the old and new passwords
     * @return a {@link ResponseEntity} containing an {@link APIResponse} indicating the password change status
     */
    @PatchMapping("/me/change-password")
    public ResponseEntity<APIResponse> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}