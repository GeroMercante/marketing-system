package com.davinci.geromercante.marketing.module.product.model.mapper;

import com.davinci.geromercante.marketing.module.product.model.dto.response.ProductResponseDTO;
import com.davinci.geromercante.marketing.module.product.model.entity.Product;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class ProductToResponseDtoConverter implements Converter<Product, ProductResponseDTO> {

    @Override
    public ProductResponseDTO convert(MappingContext<Product, ProductResponseDTO> context) {
        Product source = context.getSource();
        ProductResponseDTO destination = context.getDestination() == null ? new ProductResponseDTO() : context.getDestination();

        // Mapear campos de BaseEntity
        destination.setId(source.getId());
        destination.setCreatedBy(source.getCreatedBy());
        destination.setUpdatedBy(source.getUpdatedBy());
        destination.setCreatedAt(source.getCreatedAt());
        destination.setUpdatedAt(source.getUpdatedAt());

        // Mapear campos específicos de Product
        destination.setExternalId(source.getExternalId());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setPrice(source.getPrice());
        destination.setStock(source.getStock());
        destination.setCategory(source.getCategory());
        destination.setCondition(source.getCondition());
        destination.setManufacturer(source.getManufacturer());
        destination.setDataSourceId(source.getDataSourceId());
        destination.setLastSyncDate(source.getLastSyncDate());

        return destination;
    }
} 