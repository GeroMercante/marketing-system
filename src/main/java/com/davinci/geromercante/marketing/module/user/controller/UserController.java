package com.davinci.geromercante.marketing.module.user.controller;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.common.util.JwtUtil;
import com.davinci.geromercante.marketing.infrastructure.annotation.RequiresPermission;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.infrastructure.validation.DomainValidator;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.user.model.dto.response.UserResponse;
import com.davinci.geromercante.marketing.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstant.CONTEXT_PATH + "/users")
@RequiredArgsConstructor
public class UserController {

    private final DomainValidator domainValidator;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @GetMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public PagedResponse<UserResponse> search(
            @RequestParam(name = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "searchText", required = false) String searchText,
            @RequestParam(name = "orderBy", required = false, defaultValue = AppConstant.DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = "orderDirection", required = false, defaultValue = AppConstant.DEFAULT_ORDER_DIR) String orderDirection,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        domainValidator.validatePageNumberAndSize(page, size);
        Long clientId = jwtUtil.getClientIdFromToken(authHeader);
        return userService.search(page, size, searchText, orderBy, orderDirection, clientId);
    }

    @GetMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public UserResponse getUser(@PathVariable Long id) throws MarketingException {
        return this.userService.getUser(id);
    }

    @PostMapping("/create")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public UserResponse createBackofficeUser(
            @RequestBody UserResponse userDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        domainValidator.validateEmail(userDTO.getEmail());
        Long sellerId = jwtUtil.getClientIdFromToken(authHeader);
        return this.userService.createBackofficeUser(userDTO, sellerId, jwtUtil.getRoleEnum(authHeader));
    }

    @PutMapping("/{userId}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public UserResponse updateUser(
            @PathVariable Long userId,
            @RequestBody UserResponse userDTO,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader
    ) throws MarketingException {
        Long clientId = jwtUtil.getClientIdFromToken(authHeader);
        return userService.updateUser(userId, userDTO, clientId, jwtUtil.getRoleEnum(authHeader));
    }
}
