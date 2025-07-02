package com.davinci.geromercante.marketing.module.product.model.dto.response;

import com.davinci.geromercante.marketing.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductResponseDTO extends BaseDTO {
    private String externalId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String condition;
    private String manufacturer;
    private Long dataSourceId;
    private Date lastSyncDate;
} 