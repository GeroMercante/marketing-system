package com.davinci.geromercante.marketing.module.auth.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "email_verification")
public class EmailVerification extends BaseEntity {
    private String email;
    private String password;
    private String verificationCode;
    private String status;
}
