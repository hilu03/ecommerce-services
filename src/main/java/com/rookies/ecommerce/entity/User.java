package com.rookies.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Table(name = "users")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(unique = true)
    @Email
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false, columnDefinition = "boolean default true")
    boolean isActive;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    UserProfile userProfile;

    @OneToOne(mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    Customer customer;

}
