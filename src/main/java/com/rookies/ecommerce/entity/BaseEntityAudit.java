package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntityAudit extends BaseEntity {

    @Column(name = "created_by")
    UUID createdBy;

    @Column(name = "modified_by")
    UUID modifiedBy;

}
