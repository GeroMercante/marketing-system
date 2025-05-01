package com.davinci.geromercante.marketing.module.user.controller;

import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.infrastructure.annotation.RequiresPermission;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfileDetailDTO;
import com.davinci.geromercante.marketing.module.user.model.dto.response.ProfilePermissionDTO;
import com.davinci.geromercante.marketing.module.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppConstant.CONTEXT_PATH + "/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public List<ProfileDetailDTO> getMeProfiles(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long clientId = jwtUtil.getClientIdFromToken(token);
        if (clientId == null) {
            return List.of();
        }
        return profileService.getMeProfiles(clientId);
    }

    @GetMapping("/permissions")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public List<ProfilePermissionDTO> getPermissions() {
        return this.profileService.getPermissions();
    }

    @PostMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ProfileDetailDTO createProfile(
            @RequestBody ProfileDTO profileDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        return this.profileService.createProfile(profileDTO, jwtUtil.getClientIdFromToken(authHeader));
    }

    @PutMapping("/{profileId}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ProfileDetailDTO updateProfile(
            @PathVariable Long profileId,
            @RequestBody ProfileDTO profileDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        return this.profileService.updateProfile(profileId, profileDTO, jwtUtil.getClientIdFromToken(authHeader));
    }

    @DeleteMapping("/{profileId}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public void deleteProfile(@PathVariable Long profileId) throws MarketingException {
        this.profileService.deleteProfile(profileId);
    }
}
