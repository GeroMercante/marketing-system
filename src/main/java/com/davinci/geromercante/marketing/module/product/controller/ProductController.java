package com.davinci.geromercante.marketing.module.product.controller;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.infrastructure.annotation.RequiresPermission;
import com.davinci.geromercante.marketing.infrastructure.config.constants.AppConstant;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.auth.model.enums.PermissionsEnum;
import com.davinci.geromercante.marketing.module.product.model.dto.request.ProductRequestDTO;
import com.davinci.geromercante.marketing.module.product.model.dto.response.ProductResponseDTO;
import com.davinci.geromercante.marketing.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(AppConstant.CONTEXT_PATH + "/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public PagedResponse<ProductResponseDTO> search(
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "orderBy", defaultValue = AppConstant.DEFAULT_ORDER_BY) String orderBy,
            @RequestParam(value = "orderDirection", defaultValue = AppConstant.DEFAULT_ORDER_DIR) String orderDirection
    ) {
        return productService.search(page, size, searchText, orderBy, orderDirection);
    }

    @GetMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
        ProductResponseDTO response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody ProductRequestDTO request
    ) throws DataCollectionException {
        ProductResponseDTO response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequestDTO request
    ) throws DataCollectionException {
        ProductResponseDTO response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN})
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import/csv")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<List<ProductResponseDTO>> importFromCsv(
            @RequestParam("file") MultipartFile file
    ) throws DataCollectionException {
        List<ProductResponseDTO> response = productService.importFromCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/import/excel")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<List<ProductResponseDTO>> importFromExcel(
            @RequestParam("file") MultipartFile file
    ) throws DataCollectionException {
        List<ProductResponseDTO> response = productService.importFromExcel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/datasource/{dataSourceId}")
    @RequiresPermission({PermissionsEnum.SUPERADMIN, PermissionsEnum.ADMIN})
    public ResponseEntity<List<ProductResponseDTO>> getProductsByDataSource(
            @PathVariable Long dataSourceId
    ) {
        List<ProductResponseDTO> response = productService.getProductsByDataSource(dataSourceId);
        return ResponseEntity.ok(response);
    }
} 