package com.davinci.geromercante.marketing.infrastructure.task;

import com.davinci.geromercante.marketing.module.datasource.repository.DataSourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSourceTaskRunner {

    private final DataSourceRepository dataSourceRepository;

    @Scheduled(cron = "${datasource.extraction.cron}")
    public void extractDataFromAllSources() {
        log.info("Iniciando proceso de extracción de datos de todas las fuentes");
        
        dataSourceRepository.findAll().forEach(dataSource -> {
            log.warn("TODO: Se debería estar ejecutando la extracción de datos, de: {}", dataSource.getName());
        });
        
        log.info("Proceso de extracción de datos completado");
    }
} 