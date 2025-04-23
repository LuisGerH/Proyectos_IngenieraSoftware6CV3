# Sistema de Busqueda y Recomendación de libros

## Descripción General
Este es un proyecto Spring Boot que implementa un sistema de  Busqueda y Recomendación de libros con gestión de usuarios y autenticación, roles y permisos, dockerizado para un despliegue sencillo.

## Características Principales
- Autenticación de usuarios
- Sistema de roles (ADMIN y USER)
- Gestión de perfiles de usuario
- Registro de nuevos usuarios
- Busqueda de libros
- Tema a elegir
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

## Funcionalidades de Favoritos
 
 - **Añadir a favorito desde la búsqueda de libros:** Los usuarios pueden guardar libros directamente desde los resultados de búsqueda con un solo clic en el ícono de corazón.
 
 - **Ver todos los favoritos:** Sección dedicada donde los usuarios pueden visualizar todos los libros que han guardado como favoritos, organizados en tarjetas con imagen, título y autor.
 
 - **Eliminar favoritos:** Los usuarios pueden eliminar libros de su colección de favoritos. Al hacerlo, se aplica una animación suave que reduce la tarjeta antes de eliminarla, mejorando la experiencia visual.
 
 - **Enlaces a detalles:** Cada libro tiene un botón o enlace que lleva directamente a su página en Open Library, donde se puede consultar más información.

## Funcionalidad de Recomendaciones

- **Recomendaciones personalizadas basadas en favoritos:** El sistema analiza los autores de los libros marcados como favoritos por el usuario y sugiere otros libros escritos por los mismos autores, ayudando a descubrir nuevas lecturas afines a sus intereses.

- **Integración con Open Library:** Las recomendaciones se obtienen dinámicamente a través de la API de Open Library, asegurando una base de datos actualizada y variada.

- **Filtrado inteligente:** Se evitan sugerencias duplicadas al excluir los libros que ya están en la lista de favoritos del usuario.

- **Visual atractivo:** Las recomendaciones incluyen imagen de portada, título y autor. Si no se encuentra una imagen, se muestra una por defecto.

- **Máximo de recomendaciones:** Para evitar saturación visual, se muestran hasta 10 recomendaciones por usuario, seleccionadas entre los autores más relevantes.

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
![image](https://github.com/user-attachments/assets/2f38cf5e-df30-44ce-83e3-fd2b9d71a696)

![image](https://github.com/user-attachments/assets/ad227537-7b09-409c-be21-4aac196d10fc)

![image](https://github.com/user-attachments/assets/2f423c48-bd6f-4c8d-bc81-cd957653e025)


