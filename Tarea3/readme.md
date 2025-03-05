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

- # Investigación: Explicación de los Archivos

## docker-compose.yml

El archivo `docker-compose.yml` define los servicios que se ejecutarán en contenedores Docker. En este caso, define dos servicios: la aplicación Spring Boot y la base de datos MySQL.

- **version**: Define la versión de Docker Compose que se está utilizando.
- **services**: Define los servicios que se ejecutarán.
  - **app**: Servicio de la aplicación Spring Boot.
    - **build**: Define cómo construir la imagen de Docker para este servicio.
      - **context**: El directorio de contexto para la construcción de la imagen.
      - **dockerfile**: El archivo Dockerfile que se utilizará para construir la imagen.
    - **ports**: Define los puertos que se expondrán.
    - **depends_on**: Define las dependencias de este servicio. En este caso, depende del servicio `db`.
    - **environment**: Define las variables de entorno para el servicio.
    - **networks**: Define las redes a las que pertenece el servicio.
    - **restart**: Define la política de reinicio del contenedor.
  - **db**: Servicio de la base de datos MySQL.
    - **image**: La imagen de Docker que se utilizará para este servicio.
    - **ports**: Define los puertos que se expondrán.
    - **environment**: Define las variables de entorno para el servicio.
    - **volumes**: Define los volúmenes que se montarán en el contenedor.
    - **networks**: Define las redes a las que pertenece el servicio.
    - **restart**: Define la política de reinicio del contenedor.
- **networks**: Define las redes que se utilizarán.
- **volumes**: Define los volúmenes que se utilizarán.





## Capturas de pantalla del sistema
![image](https://github.com/user-attachments/assets/abb2b3fb-ad04-40c6-ba47-61dc8903e94b)

![image](https://github.com/user-attachments/assets/1e093d7e-2b1c-4cdc-8283-ce53a5ad95dd)

![image](https://github.com/user-attachments/assets/5267f3f7-82b8-4792-b2e4-c4e6ac02090f)

![image](https://github.com/user-attachments/assets/040f3004-f0d3-43c5-bcad-484a7d6886fe)

## Capturas de pantalla pruebas de CURL

![image](https://github.com/user-attachments/assets/5bc3cfc2-4fa9-4b09-9a47-b224a7ed8aa8)

