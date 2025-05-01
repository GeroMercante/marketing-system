# Primer parcial - Diseño de sistemas

##  📌 Tecnologías

- **Spring Boot:** 3.4.5
- **Spring Security 6**

## 📌 Descargar el proyecto
```
git clone https://github.com/GeroMercante/marketing-system.git
```

## 📌 Documentación de la aplicación
```
https://pub-9ca2f14a957c40cc8370626a35fa2458.r2.dev/Dise%C3%B1o%20de%20sistemas%20-%20Ger%C3%B3nimo%20Mercante.postman_collection.json
```

## 🚀 Instalación y configuración

### 1️⃣ **Requisitos previos**

#### Si deseas levantarlo con maven:
- **Maven** (aunque los proyectos modernos de spring incorporan un mvn integrado para compilar)
- **JDK 21**
- **MySQL 8.x.x** (o superior)

#### Si deseas levantarlo con docker-compose:
- **Docker**
- **Docker Compose**


### 2️⃣ **Instalación**
### Si deseas levantarlo con maven:
```
./mvnw spring-boot:run
```

### Si deseas levantarlo con docker-compose:
```
docker-compose build
docker-compose up -d
```

## 🏗️ Levantar el proyecto en distintos entornos

El sistema maneja diferentes configuraciones según el entorno en el que se ejecute.

Puedes cambiar estas propiedades en el `application.properties`

- `native`
- `dev`
- `prod`

## 🌍 Internacionalización (i18n)

El sistema está preparado para múltiples idiomas

### Idiomas soportados
- `ES` (español)
- `EN` (inglés)

## 👨‍💻 Autor 
**Geromercante**
