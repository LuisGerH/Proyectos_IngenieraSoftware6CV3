-- Crear tabla de roles si no existe
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL UNIQUE
);

-- Crear tabla de usuarios si no existe
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    theme VARCHAR(20) DEFAULT 'system'
);

-- Crear tabla intermedia si no existe
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT,
    rol_id BIGINT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- Insertar roles de prueba (solo si no existen)
INSERT INTO roles (id, nombre) 
SELECT 1, 'ROLE_ADMIN' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ROLE_ADMIN');

INSERT INTO roles (id, nombre) 
SELECT 2, 'ROLE_USER' 
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ROLE_USER');

-- Insertar usuarios de prueba (solo si no existen)
-- Password: "test123" encriptado con BCrypt
INSERT INTO usuarios (id, nombre, email, password, theme) 
SELECT 1, 'testuser', 'test@test.com', '$2a$10$GJGcvTc.AMpmdUvt/dpW5OdvefqBgeOi1jKWuDuWxbiUMym1Alc06', 'system'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'testuser');

INSERT INTO usuarios (id, nombre, email, password, theme) 
SELECT 2, 'testadmin', 'admin@test.com', '$2a$10$GJGcvTc.AMpmdUvt/dpW5OdvefqBgeOi1jKWuDuWxbiUMym1Alc06', 'system'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'testadmin');

-- Asignar roles a usuarios (solo si no existen)
INSERT INTO usuario_roles (usuario_id, rol_id) 
SELECT 1, 2 
WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 1 AND rol_id = 2);

INSERT INTO usuario_roles (usuario_id, rol_id) 
SELECT 2, 1 
WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 2 AND rol_id = 1);

INSERT INTO usuario_roles (usuario_id, rol_id) 
SELECT 2, 2 
WHERE NOT EXISTS (SELECT 1 FROM usuario_roles WHERE usuario_id = 2 AND rol_id = 2);