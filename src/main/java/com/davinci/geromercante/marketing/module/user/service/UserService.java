package com.davinci.geromercante.marketing.module.user.service;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.module.auth.model.enums.RoleEnum;
import com.davinci.geromercante.marketing.module.user.model.dto.response.UserResponse;

public interface UserService {
    PagedResponse<UserResponse> search(int page, int size, String searchText, String orderBy, String orderDirection, Long clientId);
    UserResponse getUser(Long id) throws MarketingException;
    UserResponse createBackofficeUser(UserResponse userDTO, Long clientId, RoleEnum role) throws MarketingException;
    UserResponse updateUser(Long id, UserResponse userDTO, Long clientId, RoleEnum role) throws MarketingException;
}
