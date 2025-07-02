package com.davinci.geromercante.marketing.module.product.service.impl;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.common.service.BaseService;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.product.manager.ProductManager;
import com.davinci.geromercante.marketing.module.product.model.dto.request.ProductRequestDTO;
import com.davinci.geromercante.marketing.module.product.model.dto.response.ProductResponseDTO;
import com.davinci.geromercante.marketing.module.product.model.entity.Product;
import com.davinci.geromercante.marketing.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends BaseService implements ProductService {

    private final ProductManager productManager;

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<ProductResponseDTO> search(int page, int size, String searchText, String orderBy, String orderDirection) {
        Page<Product> productPage = productManager.search(page, size, searchText, orderBy, orderDirection);
        return PagedResponse.from(this.convertToDto(productPage.getContent(), ProductResponseDTO.class), productPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProduct(Long id) {
        Product product = productManager.getProduct(id);
        return this.convertToDto(product, ProductResponseDTO.class);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO request) throws DataCollectionException {
        Product savedProduct = productManager.createProduct(request);
        return this.convertToDto(savedProduct, ProductResponseDTO.class);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) throws DataCollectionException {
        Product updatedProduct = productManager.updateProduct(id, request);
        return this.convertToDto(updatedProduct, ProductResponseDTO.class);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        productManager.deleteProduct(id);
    }

    @Override
    @Transactional
    public List<ProductResponseDTO> importFromCsv(MultipartFile file) throws DataCollectionException {
        // TODO: Implementar importación CSV cuando las dependencias estén disponibles
        throw new DataCollectionException("Importación CSV no implementada aún", ErrorCodeResponse.BUSINESS_ERROR);
    }

    @Override
    @Transactional
    public List<ProductResponseDTO> importFromExcel(MultipartFile file) throws DataCollectionException {
        // TODO: Implementar importación Excel cuando las dependencias estén disponibles
        throw new DataCollectionException("Importación Excel no implementada aún", ErrorCodeResponse.BUSINESS_ERROR);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByDataSource(Long dataSourceId) {
        List<Product> products = productManager.getProductsByDataSource(dataSourceId);
        return this.convertToDto(products, ProductResponseDTO.class);
    }
} 