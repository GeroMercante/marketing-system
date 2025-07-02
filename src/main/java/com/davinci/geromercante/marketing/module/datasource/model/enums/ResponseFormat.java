package com.davinci.geromercante.marketing.module.datasource.model.enums;

import lombok.Getter;

@Getter
public enum ResponseFormat {
    JSON("JavaScript Object Notation"),
    XML("Extensible Markup Language"),
    CSV("Comma Separated Values");

    private final String description;

    ResponseFormat(String description) {
        this.description = description;
    }
}
