package com.davinci.geromercante.marketing.module.auth.model.entity;

import com.davinci.geromercante.marketing.common.model.entity.BaseEntity;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "credentials")
public class Credential extends BaseEntity implements UserDetails {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private CredentialTypeEnum typeCredential;

    private String externalReference;

    private Boolean isTemporary = Boolean.TRUE;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @PostLoad
    private void loadAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>();

        if (user != null && user.getRole() != null) {
            if (user.getRole().equals(RoleEnum.SUPERADMIN)) {
                list.add(new SimpleGrantedAuthority(RoleEnum.SUPERADMIN.name()));
            } else if (user.getRole().equals(RoleEnum.ADMIN)) {
                list.add(new SimpleGrantedAuthority(RoleEnum.ADMIN.name()));
            }
            this.authorities = list;
        }

        if (user != null && user.getProfile() != null) {
            for (String s : user.getProfile().getPermissionCodes()) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(s);
                list.add(simpleGrantedAuthority);
            }
            this.authorities = list;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Boolean.TRUE.equals(isTemporary) ? null : authorities;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}