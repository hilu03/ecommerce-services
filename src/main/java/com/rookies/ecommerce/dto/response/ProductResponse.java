package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {

    String id;

    String name;

    String imageUrl;

    boolean isFeatured;

    int availableQuantity;

    double price;

    String slug;

}
