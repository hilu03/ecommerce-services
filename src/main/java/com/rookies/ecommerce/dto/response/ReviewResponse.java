package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {

    UUID id;

    int rating;

    String comment;

    Instant createdAt;

    Instant updatedAt;

    CustomerDTO customer;

}
