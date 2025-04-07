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

- **Página principal:** Al ingresar al portal, los usuarios pueden ver una sección de bienvenida con una breve descripción de la plataforma, junto con un campo de búsqueda para ingresar un título, autor o ISBN del libro que desean encontrar.
  
- **Resultados de búsqueda:** Después de ingresar una consulta, los resultados se mostrarán en una serie de tarjetas con imágenes de portada de los libros, el nombre del autor y el año de publicación. Los resultados también indican si un libro es un **CLÁSICO** basado en su fecha de publicación.

- **Estado vacío:** Si no se encuentran libros relacionados con la búsqueda, se muestra un mensaje amigable animando al usuario a realizar una nueva búsqueda.

- **Pantalla de carga:** Durante la búsqueda, se muestra un spinner de carga para que el usuario sepa que la plataforma está procesando su consulta.

- **Error en la búsqueda:** Si ocurre un error durante la búsqueda (por ejemplo, problemas con la conexión a la API), se mostrará un mensaje de error indicando al usuario que intente de nuevo.


## Instalación y Ejecución

### Método 1: Ejecución Local
1. Clonar el repositorio
```bash
git clone <https://github.com/LuisGerH/Proyectos_IngenieraSoftware6CV3/tree/main/Tarea3>
cd HOLASPRING6CV3
```

2. Correr docker
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

