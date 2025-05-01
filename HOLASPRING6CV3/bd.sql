-- Eliminar la base de datos "tarea2" si ya existe
DROP DATABASE IF EXISTS tarea2;
-- Crear la base de datos "tarea2" con codificación UTF-8
CREATE DATABASE tarea2 CHARACTER SET utf8 COLLATE utf8_general_ci;
-- Usar la base de datos "tarea2"
USE tarea2;
-- Crear la tabla de usuarios
CREATE TABLE usuarios (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(64) NOT NULL,
email VARCHAR(64) NOT NULL UNIQUE,
password VARCHAR(128) NOT NULL
);
-- Crear la tabla de roles
CREATE TABLE roles (
id BIGINT PRIMARY KEY AUTO_INCREMENT,
nombre VARCHAR(64) NOT NULL UNIQUE
);
-- Crear la tabla intermedia para la relación muchos a muchos entre usuarios y roles
CREATE TABLE usuario_roles (
usuario_id BIGINT,
rol_id BIGINT,
PRIMARY KEY (usuario_id, rol_id),
FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE libros_favoritos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    libro_id VARCHAR(64) NOT NULL,    -- ID del libro de Open Library
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255),
    imagen_url VARCHAR(255),
    fecha_guardado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    UNIQUE KEY unique_usuario_libro (usuario_id, libro_id)
);

-- Insertar roles en la tabla roles
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN'), ('ROLE_USER');
-- Eliminar el usuario admin si ya existe (para evitar duplicados)
DELETE FROM usuario_roles WHERE usuario_id IN (SELECT id FROM usuarios WHERE email = 'admin@admin.com');
DELETE FROM usuarios WHERE email = 'admin@admin.com';

-- Insertar un usuario administrador
INSERT INTO usuarios (nombre, email, password)
VALUES ('admin', 'admin@admin.com', '$2a$10$GJGcvTc.AMpmdUvt/dpW5OdvefqBgeOi1jKWuDuWxbiUMym1Alc06');
-- La contraseña es 'admin' encriptada con BCrypt

-- Obtener el ID del usuario recién creado
SET @admin_id = LAST_INSERT_ID();

-- Asignar el rol ADMIN al usuario (asumiendo que ROLE_ADMIN tiene ID=1)
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES (@admin_id, 1);

-- Eliminar el usuario 'admin' si ya existe (para cualquier host)
DROP USER IF EXISTS 'admin'@'%';
DROP USER IF EXISTS 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Crear el usuario 'admin' que puede acceder desde cualquier host
CREATE USER 'admin'@'%' IDENTIFIED BY 'admin';

-- Otorgar todos los permisos sobre la base de datos "tarea2" al usuario 'admin'
GRANT ALL PRIVILEGES ON tarea2.* TO 'admin'@'%';

-- Aplicar los cambios
FLUSH PRIVILEGES;