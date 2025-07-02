package com.davinci.geromercante.marketing.module.datasource.controller;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.infrastructure.annotation.RequiresPermission;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.infrastructure.exception.MarketingException;
import com.davinci.geromercante.marketing.infrastructure.validation.DomainValidator;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.datasource.model.dto.request.DataSourceRequestDTO;
import com.davinci.geromercante.marketing.module.datasource.model.dto.response.DataSourceResponseDTO;
import com.davinci.geromercante.marketing.module.datasource.service.DataSourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstant.CONTEXT_PATH + "/data-sources")
@RequiredArgsConstructor
public class DataSourceController {

    private final DataSourceService dataSourceService;
    private final DomainValidator domainValidator;

    @GetMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<PagedResponse<DataSourceResponseDTO>> search(
            @RequestParam(name = "page", required = false, defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(name = "size", required = false, defaultValue = AppConstant.DEFAULT_PAGE_SIZE) Integer size,
            @RequestParam(name = "searchText", required = false) String searchText,
            @RequestParam(name = "orderBy", required = false, defaultValue = AppConstant.DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(name = "orderDirection", required = false, defaultValue = AppConstant.DEFAULT_ORDER_DIR) String orderDirection
    ) throws DataCollectionException, MarketingException {
        domainValidator.validatePageNumberAndSize(page, size);
        return ResponseEntity.ok(dataSourceService.search(page, size, searchText, orderBy, orderDirection));
    }

    @GetMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<DataSourceResponseDTO> getDataSource(@PathVariable Long id) throws DataCollectionException {
        return ResponseEntity.ok(dataSourceService.getDataSource(id));
    }

    @PostMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<DataSourceResponseDTO> createDataSource(
            @RequestBody DataSourceRequestDTO request
    ) throws DataCollectionException {
        DataSourceResponseDTO response = dataSourceService.createDataSource(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<DataSourceResponseDTO> updateDataSource(
            @PathVariable Long id,
            @RequestBody DataSourceRequestDTO request
    ) throws DataCollectionException {
        return ResponseEntity.ok(dataSourceService.updateDataSource(id, request));
    }

    @DeleteMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<Void> deleteDataSource(@PathVariable Long id) throws DataCollectionException {
        dataSourceService.deleteDataSource(id);
        return ResponseEntity.noContent().build();
    }
} 