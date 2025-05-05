package com.rookies.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateUpdateCategoryRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    String name;

    String description;

}
