package com.rookies.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shipping_addresses")
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShippingAddress extends BaseEntity {

    @Column(nullable = false)
    String receiverName;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    String address;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String ward;

    @Column
    boolean isDefault;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Customer customer;

    @OneToMany(mappedBy = "address",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JsonIgnore
    List<Order> orders;

}
