# README - Documentación de Pruebas Realizadas

## 📋 Resumen General

Este proyecto implementa un sistema completo de pruebas para una aplicación web desarrollada en Spring Boot con Flutter como frontend. Se han ejecutado múltiples tipos de pruebas para garantizar la calidad, funcionalidad y rendimiento del sistema.

## 🧪 Tipos de Pruebas Implementadas

### 1. **Pruebas Unitarias**
- **Clase probada**: `LibroFavoritoService`
- **Método evaluado**: `esFavorito()`
- **Framework**: JUnit 5 + Mockito
- **Cobertura**: 100% (3/3 escenarios)
- **Casos de prueba**:
  - Usuario existe + Libro es favorito → `true`
  - Usuario existe + Libro NO es favorito → `false`
  - Usuario NO existe → `false`

**Resultados**: ✅ **3/3 pruebas exitosas** - Tiempo: 0.996s

### 2. **Pruebas de Integración**
- **Capas probadas**: Repositorio, Servicio, Controlador REST
- **Base de datos**: H2 (en memoria)
- **Framework**: Spring Boot Test + MockMvc
- **Casos evaluados**:
  - Operaciones CRUD completas
  - Validaciones de datos
  - Manejo de errores
  - Autenticación y autorización

**Resultados**: ✅ **20+ pruebas exitosas** - Tiempo: 7.228s

### 3. **Pruebas de Aceptación del Usuario (UAT)**
- **Entorno**: Aplicación Flutter + Backend dockerizado
- **Casos probados**:
  - ✅ Inicio de sesión con credenciales válidas
  - ✅ Prueba de conexión backend-frontend
  - ✅ Registro de nuevos usuarios
  - ✅ Búsqueda de libros con metadatos

  ## 🤝 Video de la aplicación móvil

- Drive: https://drive.google.com/drive/folders/1fA9tqzwu3qnHgKmel-enF06YqaEVdwkD?usp=drive_link

**Resultados**: ✅ **Todas las funcionalidades cumplieron los requisitos**

### 4. **Pruebas de Rendimiento**
- **Herramienta**: Apache JMeter 5.6.3
- **Configuración**: 10 usuarios concurrentes por 30 segundos
- **Endpoints evaluados**:
  - `GET /login` - Página de inicio
  - `POST /procesar_login` - Autenticación

**Resultados**:
- ⚡ **Tiempo de respuesta promedio**: 6ms
- 🎯 **Throughput**: 4.86 requests/segundo
- ✅ **Tasa de error**: 0% (cero errores)
- 📈 **Total de muestras**: 137 peticiones

## 📊 Métricas de Calidad

| Tipo de Prueba | Casos Ejecutados | Éxito | Tiempo Ejecución |
|----------------|------------------|-------|------------------|
| Unitarias | 3 | 100% | 0.996s |
| Integración | 20+ | 100% | 7.228s |
| UAT | 4 | 100% | Manual |
| Rendimiento | 137 muestras | 100% | 30s |

## 🏗️ Arquitectura de Pruebas

```
┌─────────────────┐
│   Pruebas UAT   │ ← Flutter App + Docker Backend
├─────────────────┤
│ Pruebas Rendim. │ ← JMeter + HTTP Endpoints
├─────────────────┤
│ Pruebas Integr. │ ← Spring Boot + H2 + MockMvc
├─────────────────┤
│ Pruebas Unit.   │ ← JUnit 5 + Mockito
└─────────────────┘
```

## 🔧 Tecnologías Utilizadas

- **Backend**: Spring Boot 3.x, Spring Security
- **Frontend**: Flutter
- **Base de Datos**: MySQL (prod), H2 (test)
- **Testing**: JUnit 5, Mockito, AssertJ, MockMvc
- **Rendimiento**: Apache JMeter 5.6.3
- **Containerización**: Docker

## 📁 Estructura de Archivos

```
├── src/test/java/
│   ├── LibroFavoritoServiceTest.java      # Pruebas unitarias
│   └── UsuarioServiceIntegrationTest.java # Pruebas integración
├── src/main/java/
│   └── UserRestController.java            # Controlador REST
├── test-configs/
│   └── logintest.jmx                      # Configuración JMeter
└── reports/
    ├── Pruebas de Aceptación del Usuario.pdf
    ├── Reporte de cobertura de código.pdf
    ├── Reporte de Pruebas de Integración.pdf
    └── Reporte de Pruebas de Rendimiento.pdf
```

## ✨ Logros Destacados

### 🎯 **Cobertura Completa**
- **100% de cobertura** en el método `esFavorito()`
- **Todos los escenarios críticos** probados
- **Optimizaciones verificadas** (no consulta favoritos si usuario no existe)

### 🚀 **Rendimiento Excelente**
- **Tiempos de respuesta < 10ms** promedio
- **Cero errores** bajo carga concurrente
- **Throughput satisfactorio** para la carga esperada

### 🔒 **Seguridad Validada**
- **Autenticación funcional** con roles
- **Autorización por endpoints** verificada
- **Validación de datos** implementada

### 🔄 **Integración Exitosa**
- **Comunicación Flutter-Backend** estable
- **Persistencia de sesión** local funcional
- **APIs RESTful** completamente operativas

## 🎉 Conclusiones

El sistema ha demostrado **excelente calidad** y **robustez** en todas las áreas evaluadas:

- ✅ **Funcionalidad**: Todos los casos de uso principales funcionan correctamente
- ✅ **Rendimiento**: Tiempos de respuesta óptimos bajo carga
- ✅ **Confiabilidad**: Cero errores en todas las pruebas
- ✅ **Integración**: Comunicación fluida entre componentes

## 🔮 Próximos Pasos

- Implementar herramientas de cobertura como JaCoCo
- Realizar pruebas de estrés con mayor carga
- Agregar pruebas de seguridad más específicas
- Implementar monitoreo de recursos del sistema

---

## Evidencia del Proceso de QA

### 📸 Captura 1: Protección de Rama
![image](https://github.com/user-attachments/assets/a243c238-5933-4972-81d2-9834e94ec431)
![image](https://github.com/user-attachments/assets/9b894462-e012-4168-9049-3e14f9c39ffd)


### 📸 Captura 2: Pull Request Creado
![image](https://github.com/user-attachments/assets/d52c9864-1651-49cc-a632-8f9d35da515d)
![image](https://github.com/user-attachments/assets/5628c980-d919-4acb-b490-846c8dfb7d8a)
![image](https://github.com/user-attachments/assets/73319b18-75e0-4ddf-9734-fbcc9f297222)


### 📸 Captura 3: Revisión y Aprobación
![image](https://github.com/user-attachments/assets/f7a8237a-3c63-453e-a607-15c0879a2a48)


### 📸 Captura 4: Merge Exitoso
![image](https://github.com/user-attachments/assets/26d6e9ff-ce26-4c2a-87cd-1e5d11ee16e5)



**Fecha**: 22 de junio de 2025  
**Estado**: ✅ **APROBADO PARA PRODUCCIÓN**  
**Equipo**: QA Testing Team
