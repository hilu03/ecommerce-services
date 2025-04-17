package com.rookies.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateCartItemRequest {

    @NotNull
    UUID productId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    int quantity;

}
