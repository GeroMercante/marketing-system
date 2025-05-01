package com.davinci.geromercante.marketing.module.auth.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "profiles_permissions")
public class ProfilePermission extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "profiles_id")
    private Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_code")
    private PermissionsEnum permissionCode;
}
