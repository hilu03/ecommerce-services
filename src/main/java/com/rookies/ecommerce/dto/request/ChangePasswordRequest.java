package com.rookies.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotNull(message = "Old password cannot be null")
    @NotBlank(message = "Old password cannot be blank")
    String oldPassword;

    @NotNull(message = "New password cannot be null")
    @NotBlank(message = "New password cannot be blank")
    String newPassword;

}
