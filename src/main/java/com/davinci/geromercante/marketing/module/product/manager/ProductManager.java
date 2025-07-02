package com.davinci.geromercante.marketing.module.product.manager;

import com.davinci.geromercante.marketing.common.util.EntitySortUtil;
import com.davinci.geromercante.marketing.common.util.MessageUtil;
import com.davinci.geromercante.marketing.infrastructure.exception.DataCollectionException;
import com.davinci.geromercante.marketing.common.model.enums.ErrorCodeResponse;
import com.davinci.geromercante.marketing.module.product.model.dto.request.ProductRequestDTO;
import com.davinci.geromercante.marketing.module.product.model.entity.Product;
import com.davinci.geromercante.marketing.module.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductManager {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MessageUtil messageUtil;

    public Page<Product> search(int page, int size, String searchText, String orderBy, String orderDirection) {
        Sort sort = EntitySortUtil.getSortForEntity(Product.class, orderBy, orderDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        return productRepository.search(searchText, pageable);
    }

    public Product getProduct(Long id) throws DataCollectionException {
        return productRepository.findById(id)
          .orElseThrow(() -> new DataCollectionException(
                  messageUtil.getMessage("product.not.found"),
                  ErrorCodeResponse.BUSINESS_ERROR
          ));
    }

    public Product createProduct(ProductRequestDTO productDto) throws DataCollectionException {
        checkUniqueConstraints(productDto, null);

        Product product = modelMapper.map(productDto, Product.class);
        product.setLastSyncDate(new Date());

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductRequestDTO productDto) throws DataCollectionException {
        Product existingProduct = getProduct(id);
        checkUniqueConstraints(productDto, id);
        modelMapper.map(productDto, existingProduct);
        existingProduct.setLastSyncDate(new Date());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) throws DataCollectionException {
        Product product = getProduct(id);
        product.setDeleted(true);
        productRepository.save(product);
    }

    public List<Product> getProductsByDataSource(Long dataSourceId) {
        return productRepository.findByDataSourceIdAndDeleted(dataSourceId, false);
    }

    public Product saveProduct(Product product) {
        product.setLastSyncDate(new Date());
        return productRepository.save(product);
    }

    private void checkUniqueConstraints(ProductRequestDTO productDto, Long excludeId) throws DataCollectionException {
        Optional<Product> existingProduct = productRepository
                .findByExternalIdAndDataSourceIdAndDeleted(productDto.getExternalId(), productDto.getDataSourceId(), false);

        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(excludeId)) {
            throw new DataCollectionException(
                    messageUtil.getMessage("product.validation.externalId.duplicate"),
                    ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    }
} 