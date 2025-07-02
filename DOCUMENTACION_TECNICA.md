# Documentación Técnica - Módulo DataSource
## Sistema de Marketing Digital - Gestión de Fuentes de Datos

### Índice
1. [Principios SOLID](#principios-solid)
2. [Patrones de Diseño](#patrones-de-diseño)
3. [Arquitectura](#arquitectura)
4. [Decisiones Técnicas](#decisiones-técnicas)

---

## Principios SOLID

### 1. Single Responsibility Principle (SRP)

**Definición:** Cada clase debe tener una única razón para cambiar, es decir, debe tener una sola responsabilidad.

**Implementación en el proyecto:**

```java
// DataSourceManager.java - Responsabilidad: Gestionar operaciones de DataSource
@Service
public class DataSourceManager {
    // Solo maneja operaciones CRUD y validaciones de DataSource
    public DataSource createDataSource(DataSource dataSource) { ... }
    public DataSource updateDataSource(Long id, DataSource dataSource) { ... }
    private void validateDataSource(DataSource dataSource) { ... }
}

// DataSourceMapper.java - Responsabilidad: Mapear entre DTOs y Entidades
@Component
public class DataSourceMapper {
    // Solo se encarga de conversiones de datos
    public DataSource toEntity(DataSourceRequestDTO dto) { ... }
    public DataSourceResponseDTO toResponseDTO(DataSource entity) { ... }
}
```

**Justificación:** Cada clase tiene una responsabilidad específica y bien definida:
- `DataSourceService`: Orquesta casos de uso
- `DataSourceManager`: Operaciones de repositorio y validaciones de dominio
- `DataSourceMapper`: Transformación de datos
- `DataSourceController`: Manejo de peticiones HTTP

### 2. Open/Closed Principle (OCP)

**Definición:** Las entidades de software deben estar abiertas para extensión, pero cerradas para modificación.

**Implementación en el proyecto:**

```java
// AuthenticationType.java - Extensible mediante enums
public enum AuthenticationType {
    NONE("No authentication required"),
    API_KEY("API Key authentication"),
    BEARER_TOKEN("Bearer token authentication"),
    BASIC_AUTH("Basic authentication");
    // Fácil agregar nuevos tipos sin modificar código existente
}

// ResponseFormat.java - Extensible para nuevos formatos
public enum ResponseFormat {
    JSON("JavaScript Object Notation"),
    XML("eXtensible Markup Language"),
    CSV("Comma Separated Values");
    // Se pueden agregar nuevos formatos (YAML, etc.) sin cambiar el código
}
```

**Justificación:** El sistema está diseñado para soportar nuevos tipos de autenticación y formatos de respuesta sin modificar el código existente, solo agregando nuevos valores a los enums.

### 3. Liskov Substitution Principle (LSP)

**Implementación en el proyecto:**

```java
// DataSourceServiceImpl.java implementa DataSourceService
@Service
public class DataSourceServiceImpl extends BaseService implements DataSourceService {
    // Puede ser sustituida por cualquier implementación de DataSourceService
    // sin romper la funcionalidad
}

// DataSource extiende BaseEntity
@Entity
public class DataSource extends BaseEntity {
    // Puede ser usada donde se espere BaseEntity
    // Hereda auditoría y funcionalidad común
}
```

**Justificación:** Las implementaciones pueden ser sustituidas por sus abstracciones sin alterar el comportamiento del sistema.

### 4. Interface Segregation Principle (ISP)

**Implementación en el proyecto:**

```java
// DataSourceService.java - Interfaz específica para DataSource
public interface DataSourceService {
    PagedResponse<DataSourceResponseDTO> search(...);
    DataSourceResponseDTO getDataSource(Long id);
    DataSourceResponseDTO createDataSource(DataSourceRequestDTO request);
    DataSourceResponseDTO updateDataSource(Long id, DataSourceRequestDTO request);
    void deleteDataSource(Long id);
}
```

**Justificación:** Cada servicio tiene su propia interfaz con métodos específicos a su dominio, evitando interfaces "gordas" que fuercen a implementar métodos innecesarios.

### 5. Dependency Inversion Principle (DIP)

**Implementación en el proyecto:**

```java
// DataSourceServiceImpl depende de abstracciones, no de concreciones
@Service
public class DataSourceServiceImpl implements DataSourceService {
    private final DataSourceManager dataSourceManager;  // Abstracción
    private final DataSourceMapper dataSourceMapper;    // Abstracción
    
    // Constructor injection - Inversión de dependencias
    public DataSourceServiceImpl(DataSourceManager manager, DataSourceMapper mapper) {
        this.dataSourceManager = manager;
        this.dataSourceMapper = mapper;
    }
}
```

**Justificación:** Las clases de alto nivel no dependen de clases de bajo nivel, ambas dependen de abstracciones mediante inyección de dependencias.

---

## Patrones de Diseño

### 1. Repository Pattern

**Motivación:** Encapsular la lógica de acceso a datos y proporcionar una interfaz más orientada a objetos para acceder a los datos.

**Implementación:**
```java
@Repository
public interface DataSourceRepository extends BaseRepository<DataSource, Long> {
    Page<DataSource> search(String searchText, Pageable pageable);
    List<DataSource> findByActiveAndDeleted(Boolean active, Boolean deleted);
    Optional<DataSource> findByNameAndDeleted(String name, Boolean deleted);
}
```

**Justificación:** Abstrae el acceso a datos, permite testing con mocks y centraliza queries específicas del dominio.

### 2. Data Transfer Object (DTO) Pattern

**Motivación:** Transferir datos entre capas sin exponer la estructura interna de las entidades.

**Implementación:**
```java
// Request DTO - Solo datos necesarios para crear/actualizar
public class DataSourceRequestDTO {
    private String name;
    private String url;
    private ResponseFormat format;
    private List<FieldMappingDTO> fieldMappings;
}

// Response DTO - Datos seguros para exponer al cliente
public class DataSourceResponseDTO extends BaseDTO {
    private String name;
    private String url;
    private Boolean active;
    // NO incluye información sensible como passwords
}
```

**Justificación:** Separa la representación interna de la externa, permitiendo evolución independiente y control sobre qué datos se exponen.

### 3. Mapper Pattern

**Motivación:** Centralizar la lógica de conversión entre DTOs y entidades.

**Implementación:**
```java
@Component
public class DataSourceMapper {
    public DataSource toEntity(DataSourceRequestDTO dto) {
        // Mapeo bidireccional con field mappings
        DataSource entity = modelMapper.map(dto, DataSource.class);
        if (dto.getFieldMappings() != null) {
            List<FieldMapping> fieldMappings = dto.getFieldMappings().stream()
                .map(fieldMappingDTO -> {
                    FieldMapping fieldMapping = modelMapper.map(fieldMappingDTO, FieldMapping.class);
                    fieldMapping.setDataSource(entity);
                    return fieldMapping;
                })
                .collect(Collectors.toList());
            entity.getFieldMappings().addAll(fieldMappings);
        }
        return entity;
    }
}
```

**Justificación:** Centraliza transformaciones complejas, maneja relaciones bidireccionales y mantiene limpia la lógica de conversión.

### 4. Facade Pattern

**Motivación:** Proporcionar una interfaz simplificada para operaciones complejas del subsistema.

**Implementación:**
```java
@Service
public class DataSourceManager {
    // Facade que combina validación + persistencia + verificación de restricciones
    public DataSource createDataSource(DataSource dataSource) throws DataCollectionException {
        validateDataSource(dataSource);           // Validación de negocio
        checkUniqueConstraints(dataSource, null); // Verificación de unicidad
        return dataSourceRepository.save(dataSource); // Persistencia
    }
}
```

**Justificación:** Simplifica operaciones complejas combinando múltiples pasos en una sola interfaz coherente.

### 5. Exception Handling Pattern

**Motivación:** Manejo centralizado y consistente de errores.

**Implementación:**
```java
// Excepción específica del dominio
public class DataCollectionException extends RuntimeException {
    private final ErrorCodeResponse errorCode;
    
    public DataCollectionException(String message, ErrorCodeResponse errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}

// Manejo centralizado
@RestControllerAdvice
public class RestControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseErrorDTO> handleException(Exception e) {
        if (e instanceof DataCollectionException dataCollectionException) {
            // Manejo específico con logging y códigos de error
            log.error("Error en recolección de datos: {}", dataCollectionException.getMessage());
            // ... crear respuesta apropiada
        }
    }
}
```

**Justificación:** Centraliza el manejo de errores, proporciona códigos semánticos y evita duplicación en controllers.

---

## Arquitectura

### Patrón: Layered Architecture + RESTful Services

**Justificación de la elección:**

1. **Separación de responsabilidades:** Cada capa tiene una responsabilidad específica
2. **Mantenibilidad:** Cambios en una capa no afectan otras
3. **Testabilidad:** Cada capa puede ser testeada independientemente
4. **Escalabilidad:** Permite evolución independiente de cada capa

**Estructura implementada:**

```
┌─────────────────────────────────────┐
│           Controller Layer          │ ← HTTP/REST interface
├─────────────────────────────────────┤
│            Service Layer            │ ← Business orchestration
├─────────────────────────────────────┤
│            Manager Layer            │ ← Domain operations facade
├─────────────────────────────────────┤
│          Repository Layer           │ ← Data access abstraction
├─────────────────────────────────────┤
│            Entity Layer             │ ← Domain model
└─────────────────────────────────────┘
```

**Flujo de datos:**
1. **Controller** recibe HTTP request
2. **Service** orquesta el caso de uso
3. **Manager** ejecuta operaciones de dominio
4. **Repository** persiste/recupera datos
5. **Mapper** convierte entre DTOs y entidades

### Ejemplo de Implementación del Flujo:

```java
// 1. Controller - Punto de entrada HTTP
@PostMapping
public ResponseEntity<DataSourceResponseDTO> createDataSource(
    @RequestBody DataSourceRequestDTO request
) throws DataCollectionException {
    DataSourceResponseDTO response = dataSourceService.createDataSource(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

// 2. Service - Orquestación del caso de uso
@Override
@Transactional
public DataSourceResponseDTO createDataSource(DataSourceRequestDTO request) {
    DataSource dataSource = dataSourceMapper.toEntity(request);      // Conversión
    DataSource savedDataSource = dataSourceManager.createDataSource(dataSource); // Lógica de negocio
    return dataSourceMapper.toResponseDTO(savedDataSource);          // Conversión de salida
}

// 3. Manager - Operaciones de dominio
public DataSource createDataSource(DataSource dataSource) throws DataCollectionException {
    validateDataSource(dataSource);           // Validaciones de negocio
    checkUniqueConstraints(dataSource, null); // Reglas de dominio
    return dataSourceRepository.save(dataSource); // Persistencia
}
```

---

## Decisiones Técnicas

### 1. Gestión de Relaciones Bidireccionales

**Decisión:** Manejo manual de relaciones `DataSource` ↔ `FieldMapping`

**Implementación:**
```java
// En DataSource entity
public void addFieldMapping(FieldMapping fieldMapping) {
    fieldMappings.add(fieldMapping);
    fieldMapping.setDataSource(this);
}

// En Mapper
if (dto.getFieldMappings() != null) {
    List<FieldMapping> fieldMappings = dto.getFieldMappings().stream()
        .map(fieldMappingDTO -> {
            FieldMapping fieldMapping = modelMapper.map(fieldMappingDTO, FieldMapping.class);
            fieldMapping.setDataSource(entity); // Establecer relación bidireccional
            return fieldMapping;
        })
        .collect(Collectors.toList());
    entity.getFieldMappings().addAll(fieldMappings);
}
```

**Justificación:** Asegura consistencia en relaciones bidireccionales y evita problemas de sincronización entre entidades relacionadas.

### 2. Validaciones de Dominio

**Decisión:** Validaciones en el Manager, no en el Controller

**Implementación:**
```java
private void validateDataSource(DataSource dataSource) throws DataCollectionException {
    // Validar URL
    try {
        URL url = new URL(dataSource.getUrl());
        String protocol = url.getProtocol().toLowerCase();
        if (!protocol.equals("http") && !protocol.equals("https")) {
            throw new DataCollectionException(
                messageUtil.getMessage("datasource.url.invalid.protocol"), 
                ErrorCodeResponse.BUSINESS_ERROR
            );
        }
    } catch (MalformedURLException e) {
        throw new DataCollectionException(
            messageUtil.getMessage("datasource.url.invalid"), 
            ErrorCodeResponse.BUSINESS_ERROR
        );
    }
}
```

**Justificación:** Las validaciones de dominio pertenecen a la lógica de negocio, no a la capa de presentación. Esto permite reutilización en diferentes puntos de entrada.

### 3. Internacionalización

**Decisión:** Mensajes centralizados con i18n

**Implementación:**
```properties
# messages_es.properties
datasource.not.found=La fuente de datos no existe.
datasource.url.invalid=La URL no es válida.

# messages_en.properties  
datasource.not.found=Data source not found.
datasource.url.invalid=URL is invalid.
```

**Justificación:** Permite soporte multi-idioma y centraliza todos los mensajes del sistema para fácil mantenimiento.

### 4. Seguridad y Autorización

**Decisión:** Solo SUPERADMIN puede gestionar DataSources

**Implementación:**
```java
@GetMapping
@RequiresPermission({PermissionsEnum.SUPERADMIN})
public PagedResponse<DataSourceResponseDTO> search(...) {
    // Solo usuarios con rol SUPERADMIN pueden acceder
}
```

**Justificación:** Los DataSources son configuraciones críticas del sistema que solo deben ser gestionadas por usuarios con máximos privilegios.

### 5. Transaccionalidad

**Decisión:** Transacciones a nivel de Service

**Implementación:**
```java
@Override
@Transactional
public DataSourceResponseDTO createDataSource(DataSourceRequestDTO request) {
    // Toda la operación es atómica
}

@Override
@Transactional(readOnly = true)
public PagedResponse<DataSourceResponseDTO> search(...) {
    // Operaciones de solo lectura optimizadas
}
```

**Justificación:** Garantiza consistencia de datos y permite rollback automático en caso de errores, especialmente importante al manejar relaciones con FieldMappings.

---

## Refactoring de Calidad de Software

### Mejoras Implementadas

#### 1. Chain of Responsibility para Validaciones

**Motivación:** Desacoplar validaciones del Manager y hacerlas extensibles.

**Implementación:**
```java
// Handler abstracto
public abstract class AbstractDataSourceValidationHandler implements DataSourceValidationHandler {
    private DataSourceValidationHandler next;
    
    public void validate(DataSourceRequestDTO dataSource) throws DataCollectionException {
        if (canHandle(dataSource)) {
            doValidate(dataSource);
        }
        if (next != null) {
            next.validate(dataSource);
        }
    }
}

// Validadores específicos por tipo de autenticación
@Component
public class ApiKeyValidationHandler extends AbstractDataSourceValidationHandler {
    protected boolean canHandle(DataSourceRequestDTO dataSource) {
        return AuthenticationType.API_KEY.equals(dataSource.getAuthenticationType());
    }
}
```

**Justificación:** Cada validador maneja un aspecto específico, permitiendo composición flexible y extensibilidad sin modificar código existente.

#### 2. Converters Autodescubribles

**Motivación:** Eliminar acoplamiento manual de mappers y usar autodescubrimiento de Spring.

**Implementación:**
```java
@Component
public class DataSourceToResponseDtoConverter implements Converter<DataSource, DataSourceResponseDTO> {
    @Override
    public DataSourceResponseDTO convert(MappingContext<DataSource, DataSourceResponseDTO> context) {
        // Conversión específica con manejo de relaciones bidireccionales
    }
}

// En Service: uso transparente
return this.convertToDto(savedDataSource, DataSourceResponseDTO.class);
```

**Justificación:** Spring IoC Container autodescubre los Converters y los registra en ModelMapper automáticamente, eliminando imports manuales.

#### 3. Manager con DTOs

**Motivación:** Manager debe recibir DTOs de capa superior, no entidades.

**Implementación:**
```java
public DataSource createDataSource(DataSourceRequestDTO dataSourceDto) throws DataCollectionException {
    // 1. Validar con Chain of Responsibility
    validationChainFactory.createValidationChain().validate(dataSourceDto);
    
    // 2. Convertir DTO a entidad con ModelMapper autodescubrible
    DataSource dataSource = modelMapper.map(dataSourceDto, DataSource.class);
    
    // 3. Verificar restricciones de dominio
    checkUniqueConstraints(dataSource, null);
    
    return dataSourceRepository.save(dataSource);
}
```

**Justificación:** Separación clara de responsabilidades: validación → conversión → reglas de dominio → persistencia.

---

## Conclusión

La implementación del módulo DataSource sigue principios sólidos de ingeniería de software con **refactorings de calidad**:

- **SOLID principles** aseguran mantenibilidad y extensibilidad
- **Chain of Responsibility** para validaciones desacopladas y extensibles
- **Autodiscovery Pattern** para conversiones transparentes
- **DTO-first approach** en capas de negocio
- **BaseService pattern** para reutilización de conversiones
- **Layered architecture** garantiza separación de responsabilidades
- **Error handling** centralizado mejora la experiencia del usuario

Estas mejoras de calidad proporcionan una base **sólida, extensible y mantenible** para las siguientes fases del sistema de recolección de datos. 