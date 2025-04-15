package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeaturedProductResponse {

    UUID id;

    Date startDate;

    Date endDate;

    String description;

    ProductResponse product;

}
