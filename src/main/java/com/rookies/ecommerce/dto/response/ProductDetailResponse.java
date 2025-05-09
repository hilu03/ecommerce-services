package com.rookies.ecommerce.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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

    BigDecimal price;

    String categoryId;

    String categoryName;

    String slug;

}
