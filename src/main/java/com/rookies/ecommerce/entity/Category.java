package com.rookies.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "categories")
public class Category extends BaseEntityAudit {

    @Column(nullable = false, unique = true)
    String name;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(columnDefinition = "TEXT")
    String slug;

    @Column(nullable = false, columnDefinition = "boolean default false")
    boolean isDeleted;

    @OneToMany(mappedBy = "category",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JsonIgnore
    List<Product> products;

}
