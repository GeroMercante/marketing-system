# Primer parcial - Diseño de sistemas

##  📌 Tecnologías

- **Spring Boot:** 3.4.5
- **Spring Security 6**

## 📌 Descargar el proyecto

- por https
```
git clone https://github.com/GeroMercante/marketing-system.git
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
docker-compose up -d
```
ó he creado un script que también hace el build y levanta el proyecto con docker-compose
```
./build_and_run.sh
```

## 🏗️ Levantar el proyecto en distintos entornos

El sistema maneja diferentes configuraciones según el entorno en el que se ejecute.

Puedes cambiar estas propiedades en el `application.properties`

- `native`
- `dev`
- `prod`

Estos scripts están en el package.json y cargan las configuraciones adecuadas desde angular.json.

## 🌍 Internacionalización (i18n)

El sistema está preparado para múltiples idiomas

### Idiomas soportados
- `ES` (español)
- `EN` (inglés)

## 👨‍💻 Autor 
**Geromercante**
