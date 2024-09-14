package com.example.chusuk.demo.config;

import lombok.Getter;

@Getter
public enum SUserGrade {
    ADMIN("ROLE_ADMIN"),
    GOLD("ROLE_USER"),
    SILVER("ROLE_USER"),
    BRONZE("ROLE_USER"),
    SELLER("ROLE_SELLER");

    // TODO 롤 구분해야하나 고민
    SUserGrade(String value) {
        this.value = value;
    }

    private String value;

}
