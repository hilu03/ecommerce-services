package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntityAudit {

    @Column(nullable = false)
    String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    String description;

    @Column(nullable = false, precision = 10, scale = 0)
    BigDecimal price;

    @Column(nullable = false)
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    int availableQuantity;

    @Column(columnDefinition = "TEXT")
    String imageUrl;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean isFeatured;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean isDeleted;

    @Column(columnDefinition = "TEXT")
    String slug;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

}
