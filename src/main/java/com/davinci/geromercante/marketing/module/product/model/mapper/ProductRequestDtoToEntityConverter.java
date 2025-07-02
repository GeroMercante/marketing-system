package com.davinci.geromercante.marketing.module.product.model.mapper;

import com.davinci.geromercante.marketing.module.product.model.dto.request.ProductRequestDTO;
import com.davinci.geromercante.marketing.module.product.model.entity.Product;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class ProductRequestDtoToEntityConverter implements Converter<ProductRequestDTO, Product> {

    @Override
    public Product convert(MappingContext<ProductRequestDTO, Product> context) {
        ProductRequestDTO source = context.getSource();
        Product destination = context.getDestination() == null ? new Product() : context.getDestination();

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

        return destination;
    }
} 