package com.davinci.geromercante.marketing.module.product.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(name = "external_id", nullable = false, length = 100)
    private String externalId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "product_condition", length = 50)
    private String condition;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "data_source_id", nullable = false)
    private Long dataSourceId;

    @Column(name = "last_sync_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastSyncDate;
} 