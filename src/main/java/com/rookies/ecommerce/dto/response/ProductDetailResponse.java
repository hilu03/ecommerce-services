package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {

    String id;

    String name;

    String description;

    String imageUrl;

    int availableQuantity;

    double price;

    String categoryId;

    String categoryName;

    String slug;

}
