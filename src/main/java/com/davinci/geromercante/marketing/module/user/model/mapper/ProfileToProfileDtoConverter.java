package com.davinci.geromercante.marketing.module.user.model.mapper;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDetailDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfilePermissionDTO;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ProfileToProfileDtoConverter implements Converter<Profile, ProfileDetailDTO> {

    private final MessageUtil messageUtil;

    public ProfileToProfileDtoConverter(@Lazy MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }

    @Override
    public ProfileDetailDTO convert(MappingContext<Profile, ProfileDetailDTO> context) {
        Profile profile = context.getSource();
        ProfileDetailDTO dto = context.getDestination() == null ? new ProfileDetailDTO() : context.getDestination();

        dto.setId(profile.getId());
        dto.setName(profile.getName());
        dto.setPermissions(profile.getProfilePermissions().stream().map(pp -> {
            ProfilePermissionDTO permissionDTO = new ProfilePermissionDTO();
            permissionDTO.setId(pp.getId());
            permissionDTO.setCode(pp.getPermissionCode());
            permissionDTO.setName(pp.getPermissionCode().getName(messageUtil));
            return permissionDTO;
        }).toList());

        return dto;
    }
}
