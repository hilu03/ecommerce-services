package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "featured_products",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "product_id", "startDate", "endDate" }) })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FeaturedProduct extends BaseEntityAudit {

    @Column(columnDefinition = "TEXT")
    String description;

    @Column
    Date startDate;

    @Column
    Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;
}
