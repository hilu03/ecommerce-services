package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Table(name = "user_profiles")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile extends BaseEntity {

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column
    String phoneNumber;

    @Column(columnDefinition = "TEXT")
    String address;

    @OneToOne(mappedBy = "userProfile",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    User user;

}
