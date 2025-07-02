package com.davinci.geromercante.marketing.module.datasource.model.enums;

import lombok.Getter;

@Getter
public enum AuthenticationType {
    NONE("No authentication required"),
    API_KEY("API Key authentication"),
    BEARER_TOKEN("Bearer token authentication"),
    BASIC_AUTH("Basic authentication");

    private final String description;

    AuthenticationType(String description) {
        this.description = description;
    }
}
