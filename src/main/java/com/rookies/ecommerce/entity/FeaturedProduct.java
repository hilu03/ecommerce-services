package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "featured_products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeaturedProduct extends BaseEntityAudit {

    @Column(columnDefinition = "TEXT")
    String description;

    @Column
    Date startDate;

    @Column
    Date endDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

}
