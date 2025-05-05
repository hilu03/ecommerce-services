package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailForAdmin {

    String id;

    String name;

    String description;

    String imageUrl;

    int availableQuantity;

    double price;

    String categoryName;

    Instant createdAt;

    Instant updatedAt;

    String createdBy;

    String modifiedBy;

}
