package com.davinci.geromercante.marketing.module.datasource.model.enums;

import lombok.Getter;

@Getter
public enum EntityType {
    PRODUCT("Product entity mapping"),
    SALE("Sale transaction mapping"),
    STOCK("Stock level mapping");

    private final String description;

    EntityType(String description) {
        this.description = description;
    }
}
