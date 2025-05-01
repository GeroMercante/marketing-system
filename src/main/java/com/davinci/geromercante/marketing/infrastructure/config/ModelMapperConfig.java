package com.davinci.geromercante.marketing.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.config.Configuration.AccessLevel;

import java.util.List;

@Slf4j
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(
            @Autowired(required = false) List<Converter<?, ?>> converters,
            @Autowired(required = false) List<AbstractConverter<?, ?>> abstractConverters,
            @Autowired(required = false) List<TypeMap<?, ?>> typeMaps
    ) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMethodAccessLevel(AccessLevel.PUBLIC);

        if (converters != null) {
            log.info("Convertidores registrados: {}", converters.size());
            converters.forEach(modelMapper::addConverter);
        }

        if (abstractConverters != null) {
            log.info("Convertidores abstractos registrados: {}", abstractConverters.size());
            abstractConverters.forEach(modelMapper::addConverter);
        }

        return modelMapper;
    }
}
