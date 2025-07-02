package com.davinci.geromercante.marketing.module.product.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {
    private String externalId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String condition;
    private String manufacturer;
    private Long dataSourceId;
}
