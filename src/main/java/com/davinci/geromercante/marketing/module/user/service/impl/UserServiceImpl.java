package com.davinci.geromercante.marketing.module.user.service.impl;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.common.service.BaseService;
import com.davinci.geromercante.marketing.common.util.GeneratorUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.infrastructure.validation.DomainValidator;
import com.davinci.geromercante.marketing.module.auth.model.dto.response.CredentialResponse;
import com.davinci.geromercante.marketing.module.auth.model.enums.CredentialTypeEnum;
import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import com.davinci.geromercante.marketing.module.user.manager.UserManager;
import com.davinci.geromercante.marketing.module.user.model.dto.response.UserResponse;
import com.davinci.geromercante.marketing.module.user.model.entity.User;
import com.davinci.geromercante.marketing.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends BaseService implements UserService {

    private final UserManager userManager;
    private final MessageUtil messageUtil;
    private final DomainValidator domainValidator;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserResponse> search(int page, int size, String searchText, String orderBy, String orderDirection, Long clientId) {
        Page<User> userPage = userManager.search(page, size, searchText, orderBy, orderDirection, clientId);
        return PagedResponse.from(this.convertToDto(userPage.getContent(), UserResponse.class), userPage);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) throws MarketingException {
        User user = userManager.getUser(userId);
        return convertToDto(user, UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse createBackofficeUser(UserResponse dto, Long clientId, RoleEnum role) throws MarketingException {
        domainValidator.validateId(clientId);

        validateAssignRole(dto, role);

        CredentialResponse credentialResponse = new CredentialResponse();
        credentialResponse.setTypeCredential(CredentialTypeEnum.INTERNAL);
        credentialResponse.setIsTemporary(Boolean.TRUE);
        credentialResponse.setIsVerified(false);
        credentialResponse.setPassword(GeneratorUtil.generateTemporaryPassword());

        User user = userManager.createUser(dto, credentialResponse);

        return this.convertToDto(user, UserResponse.class);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserResponse dto, Long sellerId, RoleEnum role) throws MarketingException {
        domainValidator.validateId(sellerId);

        validateAssignRole(dto, role);

        User user = userManager.updateUser(id, dto);

        return this.convertToDto(user, UserResponse.class);
    }
    
    private void validateAssignRole(UserResponse dto, RoleEnum userRole) throws MarketingException {
        if (userRole != null) {
            if (userRole == RoleEnum.ADMIN && dto.getRole() != null && dto.getRole().equals(RoleEnum.SUPERADMIN)) {
                throw new MarketingException(messageUtil.getMessage("exception.invalid.assignment"));
            }
        } else {
            dto.setRole(null);
        }
    }
}
