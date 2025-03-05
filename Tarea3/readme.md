# Sistema de Gestión de Usuarios con Spring Boot

## Descripción General
Este es un proyecto Spring Boot que implementa un sistema de gestión de usuarios con autenticación, roles y permisos, dockerizado para un despliegue sencillo.

## Características Principales
- Autenticación de usuarios
- Sistema de roles (ADMIN y USER)
- Gestión de perfiles de usuario
- Registro de nuevos usuarios
- Dockerización completa

## Requisitos Previos
- Java 21
- Maven
- Docker (opcional, para despliegue)

## Ejercicios Implementados

### Ejercicio 1: Preparación del Proyecto y Conexión con Base de Datos
- Configuración de conexión a MySQL
- Creación de entidades JPA (UserModel, Role)
- Definición de repositorios y servicios
- Script SQL para inicialización de base de datos

### Ejercicio 2: Implementación de Vista de Login
- Controlador de Login
- Página de login con Bootstrap
- Manejo de errores de autenticación
- Redireccionamiento según roles

### Ejercicio 3: Sistema de Autenticación, Roles y Permisos
- Configuración de Spring Security
- Implementación de CustomUserDetailsService
- Control de acceso basado en roles
- Endpoints protegidos para ADMIN y USER
- Gestión de usuarios por parte del administrador

### Ejercicio 4: Dockerización de la Aplicación
- Dockerfile para compilación y ejecución
- docker-compose.yml para orquestación de servicios
- Configuración de networking entre aplicación y base de datos

## Instalación y Ejecución

### Método 1: Ejecución Local
1. Clonar el repositorio
```bash
git clone <https://github.com/LuisGerH/Proyectos_IngenieraSoftware6CV3/tree/main/Tarea3>
cd HOLASPRING6CV3
```

2. Configurar base de datos MySQL
- Asegurarse de tener MySQL instalado
- Crear base de datos `tarea2`
- Configurar credenciales en `application.properties`

3. Compilar y ejecutar
```bash
mvn clean package
https://github.com/LuisGerH/Proyectos_IngenieraSoftware6CV3/tree/main/Tarea3
```

### Método 2: Despliegue con Docker
1. Asegurarse de tener Docker y Docker Compose instalados

2. Construir y levantar servicios
```bash
docker-compose up --build
```

## Credenciales por Defecto
- Usuario Admin: 
  - Username: admin
  - Password: admin
- Usuario Regular: 
  - Crear mediante registro en la aplicación

## Características Técnicas
- Lenguaje: Java 21
- Framework: Spring Boot 3.4.2
- Base de Datos: MySQL 8.0
- Seguridad: Spring Security
- Frontend: Thymeleaf, Bootstrap 5

## Estructura del Proyecto
```
src/
├── main/
│   ├── java/com/holamundo/HOLASPRING6CV3/
│   │   ├── config/
│   │   ├── controllers/
│   │   ├── models/
│   │   ├── repositories/
│   │   └── services/
│   └── resources/
│       ├── static/
│       └── templates/
└── docker-compose.yml
```

## Consideraciones de Seguridad
- Contraseñas encriptadas con BCrypt
- CSRF deshabilitado (configuración para desarrollo)
- Control de acceso por roles
- Validación de datos de entrada


## Capturas de pantalla del sistema

## Capturas de pantalla pruebas de CURL