package com.davinci.geromercante.marketing.module.user.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "profiles_id")
    @JsonManagedReference
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private String email;

    private Boolean active = Boolean.TRUE;

    private String firstname;

    private String lastname;

    private String documentValue;

    @Column(name = "phone_number")
    private String phoneNumber;

    private Date birthDate;

    private Boolean verifiedIdentity  = Boolean.FALSE;

    @Transient
    public String getProfileName() {
        return profile != null ? profile.getName() : null;
    }
}
