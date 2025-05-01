package com.davinci.geromercante.marketing.module.auth.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
public class Session extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "credential_id", nullable = false)
    private Credential credential;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = true)
    private Profile profile;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked = false;
}
