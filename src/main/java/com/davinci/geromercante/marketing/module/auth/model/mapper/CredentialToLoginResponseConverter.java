package com.davinci.geromercante.marketing.module.auth.model.mapper;

import com.davinci.geromercante.marketing.module.auth.model.dto.response.LoginResponse;
import com.davinci.geromercante.marketing.module.auth.model.entity.Credential;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CredentialToLoginResponseConverter implements Converter<Credential, LoginResponse> {

    @Override
    public LoginResponse convert(MappingContext<Credential, LoginResponse> context) {
        Credential credential = context.getSource();
        LoginResponse dto = context.getDestination() == null ? new LoginResponse() : context.getDestination();

        dto.setId(credential.getUser().getId());
        dto.setFirstname(credential.getUser().getFirstname());
        dto.setLastname(credential.getUser().getLastname());
        dto.setEmail(credential.getUser().getEmail());
        dto.setBirthDate(credential.getUser().getBirthDate());
        dto.setIsCredentialTemporary(credential.getIsTemporary());
        dto.setAuthorities(credential.getAuthorities() != null && !credential.getAuthorities().isEmpty()
                ? credential.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                : List.of());

        dto.setProfile(credential.getUser().getRole() != null
                ? credential.getUser().getRole().name()
                : credential.getUser().getProfile() != null
                ? credential.getUser().getProfile().getName()
                : "APP"
        );

        return dto;
    }
}
