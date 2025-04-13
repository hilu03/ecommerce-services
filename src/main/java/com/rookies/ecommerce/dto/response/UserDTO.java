package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

    String firstName;

    String lastName;

    String email;

    String role;

    Instant createdAt;

    Instant updatedAt;

    boolean active;

}
