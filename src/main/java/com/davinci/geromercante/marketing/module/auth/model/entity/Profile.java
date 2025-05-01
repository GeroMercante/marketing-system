package com.davinci.geromercante.marketing.module.auth.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "profiles")
public class Profile extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<ProfilePermission> profilePermissions = new ArrayList<>();

    public List<String> getPermissionCodes() {
        List<String> list = new ArrayList<>();
        for (ProfilePermission pp : this.getProfilePermissions()) {
            String code = pp.getPermissionCode().name();
            list.add(code);
        }
        return list;
    }
}
