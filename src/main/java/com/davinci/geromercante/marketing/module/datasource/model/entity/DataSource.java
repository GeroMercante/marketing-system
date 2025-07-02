package com.davinci.geromercante.marketing.module.datasource.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.datasource.model.enums.AuthenticationType;
import com.davinci.geromercante.marketing.module.datasource.model.enums.ResponseFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "data_sources")
public class DataSource extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "url", nullable = false, length = 2048)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private ResponseFormat format;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", nullable = false)
    private AuthenticationType authenticationType;

    @Column(name = "api_key", length = 500)
    private String apiKey;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "bearer_token", length = 1000)
    private String bearerToken;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "timeout_seconds", nullable = false)
    private Integer timeoutSeconds = 30;

    @Column(name = "retry_attempts", nullable = false)
    private Integer retryAttempts = 3;

    @OneToMany(mappedBy = "dataSource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FieldMapping> fieldMappings = new ArrayList<>();

    public void addFieldMapping(FieldMapping fieldMapping) {
        fieldMappings.add(fieldMapping);
        fieldMapping.setDataSource(this);
    }

    public void removeFieldMapping(FieldMapping fieldMapping) {
        fieldMappings.remove(fieldMapping);
        fieldMapping.setDataSource(null);
    }
} 