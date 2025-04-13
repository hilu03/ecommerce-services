package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.request.ChangePasswordRequest;
import com.rookies.ecommerce.dto.request.UpdateUserInfo;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @GetMapping("/me")
    public ResponseEntity<APIResponse> getMyInfo(@RequestParam String id) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                userService.getMyInfo(id)));
    }

    @PutMapping("/me")
    public ResponseEntity<APIResponse> updateUser(@RequestParam String id,
                                                  @RequestBody UpdateUserInfo info) {
        userService.updateUser(id, info);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    @GetMapping()
    public ResponseEntity<APIResponse> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                userService.getAllUsers(page, size, sortBy, sortDir)));
    }

    @PatchMapping("/me/change-password")
    public ResponseEntity<APIResponse> changePassword(@RequestParam String id,
                                                      @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

    @PatchMapping("/toggle-status")
    public ResponseEntity<APIResponse> toggleUserStatus(@RequestParam String id,
                                                         @RequestParam boolean status) {
        userService.toggleUserStatus(id, status);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}
