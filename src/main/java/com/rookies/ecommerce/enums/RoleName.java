package com.rookies.ecommerce.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    USER_ROLE("ROLE_USER"),
    ADMIN_ROLE("ROLE_ADMIN"),
    ;

    RoleName(String name) {
        this.name = name;
    }

    private final String name;
}
