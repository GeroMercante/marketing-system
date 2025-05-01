package com.davinci.geromercante.marketing.module.user.manager;

import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.auth.model.entity.ProfilePermission;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfilePermissionDTO;
import com.davinci.geromercante.marketing.module.user.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileManager {

    private final ProfileRepository profileRepository;
    private final MessageUtil messageUtil;

    public List<Profile> getMeProfiles(Long clientId) {
        return this.profileRepository.findAll();
    }

    public Profile getProfileStrict(Long id) throws MarketingException {
        return profileRepository.findById(id)
                .orElseThrow(() -> new MarketingException(messageUtil.getMessage("profile.not.found")));
    }

    public Profile saveProfile(ProfileDTO profileDTO, Long clientId) throws MarketingException {
        return createOrUpdateProfile(null, profileDTO, clientId);
    }

    public Profile updateProfile(Long profileId, ProfileDTO dto, Long clientId) throws MarketingException {
        return createOrUpdateProfile(profileId, dto, clientId);
    }

    public void deleteProfile(Long profileID) throws MarketingException {
        Profile profile = getProfileStrict(profileID);
        profileRepository.delete(profile);
    }

    public List<ProfilePermissionDTO> getPermissions() {
        return Arrays.stream(PermissionsEnum.values())
                .filter(permission -> permission != PermissionsEnum.SUPERADMIN && permission != PermissionsEnum.ADMIN)
                .map(permission -> ProfilePermissionDTO
                        .builder()
                        .code(permission)
                        .name(permission.getName(messageUtil))
                        .build())
                .toList();
    }

    private Profile createOrUpdateProfile(Long profileId, ProfileDTO dto, Long clientId) throws MarketingException {
        Profile profile = (profileId == null) ? new Profile() : getProfileStrict(profileId);

        if (profileId != null) {
            profile.getProfilePermissions().clear();
        }

        profile.setName(dto.getName());

        List<ProfilePermission> profilePermissions = createProfilePermissions(dto.getProfilePermissions(), profile);
        profile.getProfilePermissions().addAll(profilePermissions);

        return profileRepository.save(profile);
    }

    private List<ProfilePermission> createProfilePermissions(List<String> permissionCodes, Profile profile) {
        return permissionCodes.stream()
                .map(permissionCode -> {
                    try {
                        return getValidPermissionEnum(permissionCode);
                    } catch (MarketingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(permissionEnum -> {
                    ProfilePermission profilePermission = new ProfilePermission();
                    profilePermission.setPermissionCode(permissionEnum);
                    profilePermission.setProfile(profile);
                    return profilePermission;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private PermissionsEnum getValidPermissionEnum(String permissionCode) throws MarketingException {
        try {
            return PermissionsEnum.valueOf(permissionCode);
        } catch (IllegalArgumentException e) {
            throw new MarketingException(messageUtil.getMessage("permission.not.found", permissionCode));
        }
    }
}