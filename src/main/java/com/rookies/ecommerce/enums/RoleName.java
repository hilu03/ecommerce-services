package com.rookies.ecommerce.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    USER_ROLE("USER"),
    ADMIN_ROLE("ADMIN"),
    ;

    RoleName(String name) {
        this.name = name;
    }

    private final String name;
}
