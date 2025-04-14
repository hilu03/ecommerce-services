package com.rookies.ecommerce.controller;

import com.rookies.ecommerce.constant.MessageResponse;
import com.rookies.ecommerce.dto.response.APIResponse;
import com.rookies.ecommerce.service.user.AdminService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/admin")
public class AdminController {

    AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<APIResponse> getAllActiveUsers(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                adminService.getAllUsersByStatus(true, page, size, sortBy, sortDir)));
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<APIResponse> getAllInactiveUsers(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "createdAt") String sortBy,
                                                         @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(new APIResponse(MessageResponse.RESOURCE_FOUND,
                adminService.getAllUsersByStatus(false, page, size, sortBy, sortDir)));
    }

    @PatchMapping("/toggle-status")
    public ResponseEntity<APIResponse> toggleUserStatus(@RequestParam String id,
                                                        @RequestParam boolean status) {
        adminService.toggleUserStatus(id, status);
        return ResponseEntity.ok(new APIResponse(MessageResponse.UPDATED_SUCCESSFULLY, null));
    }

}
