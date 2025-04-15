package com.rookies.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateProductRequest {

    @NotNull
    @NotBlank
    String name;

    @NotNull
    String description;

    BigDecimal price;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    int availableQuantity;

    @NotNull
    String categoryId;

}
