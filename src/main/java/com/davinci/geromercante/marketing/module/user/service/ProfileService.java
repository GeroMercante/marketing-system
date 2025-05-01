package com.davinci.geromercante.marketing.module.user.service;

import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDetailDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfilePermissionDTO;

import java.util.List;

public interface ProfileService {
    List<ProfileDetailDTO> getMeProfiles(Long clientId);
    List<ProfilePermissionDTO> getPermissions();
    ProfileDetailDTO getProfile(Long id) throws MarketingException;
    ProfileDetailDTO createProfile(ProfileDTO dto, Long clientId) throws MarketingException;
    ProfileDetailDTO updateProfile(Long profileId, ProfileDTO dto, Long clientId) throws MarketingException;
    void deleteProfile(Long profileId) throws MarketingException;
}
