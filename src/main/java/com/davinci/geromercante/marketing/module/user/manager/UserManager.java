package com.davinci.geromercante.marketing.module.user.manager;

import com.davinci.geromercante.marketing.common.util.EntitySortUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.manager.CredentialManager;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.CredentialResponse;
import com.davinci.geromercante.marketing.module.auth.model.entity.Profile;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.user.model.dto.response.UserResponse;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import com.davinci.geromercante.marketing.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;
    private final MessageUtil messageUtil;
    private final CredentialManager credentialManager;
    private final ProfileManager profileManager;

    public User findByEmail(String email) throws MarketingException {
        return userRepository.findByEmail(email).orElseThrow(() -> new MarketingException(messageUtil.getMessage("user.not.found")));
    }

    public Page<User> search(int page, int size, String searchText, String orderBy, String orderDirection, Long clientId) {
        Sort sort = EntitySortUtil.getSortForEntity(User.class, orderBy, orderDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.search(searchText, clientId, pageable);
    }

    public User getUser(Long id) throws MarketingException {
        return userRepository.findById(id).orElseThrow(() -> new MarketingException(messageUtil.getMessage("user.not.found")));
    }

    public User createUser(UserResponse userDTO, CredentialResponse credentialDTO) throws MarketingException {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());

        if (existingUser.isPresent()) {
            throw new MarketingException(messageUtil.getMessage("user.exist"));
        }

        User user = new User();
        setUserDetails(user, userDTO);
        user.setEmail(userDTO.getEmail());

        userRepository.save(user);
        credentialManager.createCredential(user, credentialDTO);
        return user;
    }

    public User updateUser(Long id, UserResponse userDTO) throws MarketingException {
        User user = getUser(id);
        setUserDetails(user, userDTO);
        return userRepository.save(user);
    }

    private void setUserDetails(User user, UserResponse dto) throws MarketingException {
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setBirthDate(dto.getBirthDate());
        user.setDocumentValue(dto.getDocumentValue());

        if (dto.getProfileId() != null) {
            Profile profile = profileManager.getProfileStrict(dto.getProfileId());
            if (profile.getName().equals(PermissionsEnum.ADMIN.name()) || profile.getName().equals(PermissionsEnum.SUPERADMIN.name())) {
                user.setProfile(profile);
                return;
            }
            user.setProfile(profile);
        }

        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
    }
}
