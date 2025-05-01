package com.davinci.geromercante.marketing.module.user.service.impl;

import com.davinci.geromercante.marketing.common.service.BaseService;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.user.manager.ProfileManager;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDetailDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfilePermissionDTO;
import com.davinci.geromercante.marketing.module.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl extends BaseService implements ProfileService {

    private final ProfileManager profileManager;

    @Override
    @Transactional(readOnly = true)
    public List<ProfileDetailDTO> getMeProfiles(Long clientId) {
        List<Profile> profileList = profileManager.getMeProfiles(clientId);
        return this.convertToDto(profileList, ProfileDetailDTO.class);
    }

    @Override
    public List<ProfilePermissionDTO> getPermissions() {
        return profileManager.getPermissions();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDetailDTO getProfile(Long id) throws MarketingException {
        Profile profile = profileManager.getProfileStrict(id);
        return this.convertToDto(profile, ProfileDetailDTO.class);
    }

    @Override
    @Transactional
    public ProfileDetailDTO createProfile(ProfileDTO dto, Long clientId) throws MarketingException {
        Profile profile = profileManager.saveProfile(dto, clientId);
        return this.convertToDto(profile, ProfileDetailDTO.class);
    }

    @Override
    @Transactional
    public ProfileDetailDTO updateProfile(Long profileId, ProfileDTO dto, Long clientId) throws MarketingException {
        Profile profile = profileManager.updateProfile(profileId, dto, clientId);
        return this.convertToDto(profile, ProfileDetailDTO.class);
    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId) throws MarketingException {
        profileManager.deleteProfile(profileId);
    }
}
