package com.davinci.geromercante.marketing.module.datasource.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.datasource.model.enums.EntityType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "field_mappings")
public class FieldMapping extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_source_id", nullable = false)
    private DataSource dataSource;

    @Column(name = "source_field", nullable = false, length = 100)
    private String sourceField;

    @Column(name = "target_field", nullable = false, length = 100)
    private String targetField;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private EntityType entityType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired = Boolean.FALSE;

    @Column(name = "default_value", length = 500)
    private String defaultValue;

    @Column(name = "transformation_expression", length = 1000)
    private String transformationExpression;
} 