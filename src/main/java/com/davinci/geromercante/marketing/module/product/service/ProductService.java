package com.davinci.geromercante.marketing.module.product.service;

import com.davinci.geromercante.marketing.common.model.dto.PagedResponse;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.module.product.model.dto.request.ProductRequestDTO;
import com.davinci.geromercante.marketing.module.product.model.dto.response.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    PagedResponse<ProductResponseDTO> search(int page, int size, String searchText, String orderBy, String orderDirection);
    ProductResponseDTO getProduct(Long id);
    ProductResponseDTO createProduct(ProductRequestDTO request) throws DataCollectionException;
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) throws DataCollectionException;
    void deleteProduct(Long id);
    List<ProductResponseDTO> importFromCsv(MultipartFile file) throws DataCollectionException;
    List<ProductResponseDTO> importFromExcel(MultipartFile file) throws DataCollectionException;
    List<ProductResponseDTO> getProductsByDataSource(Long dataSourceId);
} 
