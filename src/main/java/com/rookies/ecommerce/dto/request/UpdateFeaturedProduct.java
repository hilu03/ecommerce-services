package com.rookies.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFeaturedProduct {

    String description;

    @NotNull
    Date startDate;

    @NotNull
    Date endDate;

}
