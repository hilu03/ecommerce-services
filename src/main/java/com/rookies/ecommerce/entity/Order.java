package com.rookies.ecommerce.entity;

import com.rookies.ecommerce.enums.OrderStatus;
import com.rookies.ecommerce.enums.PaymentMethod;
import com.rookies.ecommerce.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntityAudit {

    @Column(nullable = false)
    String receiver;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String district;

    @Column(nullable = false)
    String ward;

    @Column(nullable = false)
    String address;

    @Column(nullable = false, precision = 10, scale = 0)
    BigDecimal totalPrice;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                    CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    OrderStatus orderStatus = OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    PaymentStatus paymentStatus = PaymentStatus.PENDING;

}
