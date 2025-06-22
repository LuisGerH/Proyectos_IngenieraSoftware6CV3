# LibroSearch 📚
### Aplicación Móvil de Búsqueda de Libros

Una aplicación móvil desarrollada en Flutter que se conecta a un backend Spring Boot para gestionar autenticación de usuarios y búsqueda de libros.

## 🚀 Características

- ✅ Autenticación de usuarios segura
- ✅ Interfaz moderna con gradientes
- ✅ Búsqueda de libros en tiempo real
- ✅ Gestión de sesiones persistente
- ✅ Diseño responsive
- ✅ Integración con backend Spring Boot + MySQL

## 🛠️ Tecnologías Utilizadas

### Backend
- **Spring Boot** - Framework Java
- **MySQL** - Base de datos
- **Docker** - Containerización
- **Spring Security** - Autenticación y autorización

### Frontend Mobile
- **Flutter** - Framework de desarrollo móvil
- **Dart** - Lenguaje de programación
- **HTTP** - Cliente para API REST
- **SharedPreferences** - Almacenamiento local

## 📋 Prerrequisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/) y Docker Compose
- [Flutter SDK](https://flutter.dev/docs/get-started/install) (versión 3.0 o superior)
- [Android Studio](https://developer.android.com/studio) o dispositivo Android físico
- Un editor de código (VS Code, Android Studio, etc.)

### Verificar instalación de Flutter
```bash
flutter --version
flutter doctor
```

## 🚀 Instalación y Configuración

### Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/LuisGerH/Proyectos_IngenieraSoftware6CV3.git
cd Proyectos_IngenieraSoftware6CV3
```

### Paso 2: Ejecutar el Backend

1. **Navegar a la carpeta del backend:**
```bash
cd HOLASPRING6CV3
```

2. **Iniciar los servicios con Docker Compose:**
```bash
docker-compose up
```

3. **Verificar que el backend esté corriendo:**
   - El servidor debería estar disponible en `http://localhost:8080`
   - Verifica en la terminal que los contenedores se hayan iniciado correctamente

### Paso 3: Configurar la Aplicación Flutter

1. **Navegar a la carpeta de Flutter:**
```bash
cd ../flut_librosearch
```

2. **Instalar las dependencias:**
```bash
flutter pub get
```

3. **Configurar la dirección IP del backend:**

   **⚠️ IMPORTANTE:** Debes configurar la IP correcta según tu setup:

   - **Para emulador Android:** Usar `10.0.2.2:8080`
   - **Para dispositivo físico:** Usar la IP de tu máquina local

   Edita el archivo `lib/core/constants/api_constants.dart`:

   ```dart
   class ApiConstants {
     // Para EMULADOR Android:
     static const String baseUrl = 'http://10.0.2.2:8080';
     
     // Para DISPOSITIVO FÍSICO (cambia por tu IP local):
     // static const String baseUrl = 'http://192.168.1.XXX:8080';
     
     // Para encontrar tu IP local:
     // Windows: ipconfig
     // macOS/Linux: ifconfig
   }
   ```

### Paso 4: Preparar el Dispositivo/Emulador

#### Opción A: Usar Emulador Android
1. Abrir Android Studio
2. Ir a AVD Manager
3. Crear y ejecutar un emulador Android
4. Verificar con: `flutter devices`

#### Opción B: Usar Dispositivo Físico
1. Habilitar "Opciones de desarrollador" en tu dispositivo Android
2. Activar "Depuración USB"
3. Conectar el dispositivo por USB
4. Verificar con: `flutter devices`

### Paso 5: Ejecutar la Aplicación Flutter

```bash
flutter run
```

O para modo debug con hot reload:
```bash
flutter run --debug
```

## 🔧 Configuración de Red

### Encontrar tu IP Local

**Windows:**
```cmd
ipconfig
```
Buscar la dirección IPv4 de tu adaptador de red principal.

**macOS/Linux:**
```bash
ifconfig
```
O usando:
```bash
ip addr show
```

### Probar Conectividad

Desde tu dispositivo móvil, abre un navegador y visita:
```
http://TU_IP_LOCAL:8080
```

Si ves la página de login de Spring Boot, la configuración es correcta.

## 📱 Uso de la Aplicación

### Credenciales de Prueba
```
Usuario: admin
Contraseña: admin123
```

### Funcionalidades Disponibles

1. **Inicio de Sesión**
   - Ingresa tus credenciales
   - Usa el botón "Probar conexión" para verificar conectividad
   - La sesión se mantiene localmente

2. **Registro de Usuario**
   - Crea una nueva cuenta desde la pantalla de registro

3. **Búsqueda de Libros**
   - Busca libros por título, autor o tema
   - Visualiza resultados con portadas e información detallada

## 📸 Capturas de Pantalla

### Pantalla de Inicio de Sesión

![Pantalla de Login](![image](https://github.com/user-attachments/assets/a63958da-e614-4e55-8641-958db60be11e)
)

### Pantalla de Registro

![Pantalla de Registro](![image](https://github.com/user-attachments/assets/9072278b-b72c-4cd1-9661-cc5543b7d3c8)
)

### Pantalla de Búsqueda

![Pantalla de Búsqueda](![image](https://github.com/user-attachments/assets/1cc3aa69-684f-4056-b865-b5cc203c25af)
)
![image](https://github.com/user-attachments/assets/c8471ce2-624e-4c6b-884a-3003616c6788)


### Terminal - Backend Ejecutándose

![Backend Running](![image](https://github.com/user-attachments/assets/d0873b62-ce65-4a74-bb2a-6d8c51f94eec)
)

### Terminal - Flutter Ejecutándose

![Flutter Running](![image](https://github.com/user-attachments/assets/c4d0d421-f741-4ae6-a7b4-f441b3c3edeb)
)

## 🐛 Solución de Problemas

### Problema: No se puede conectar al backend

**Síntomas:**
- Error de conexión en la app móvil
- Timeout en las peticiones HTTP

**Soluciones:**
1. Verificar que Docker esté ejecutándose: `docker ps`
2. Confirmar que el backend esté en puerto 8080: `curl http://localhost:8080`
3. Revisar la configuración de IP en `api_constants.dart`
4. Asegurarse de que el dispositivo/emulador esté en la misma red

### Problema: El emulador no se conecta

**Solución:**
- Usar la IP especial del emulador: `10.0.2.2:8080`
- Verificar que el emulador tenga conexión a internet

### Problema: Dispositivo físico no se conecta

**Soluciones:**
1. Verificar que estén en la misma red WiFi
2. Usar la IP local correcta de la máquina host
3. Desactivar temporalmente el firewall para pruebas

### Problema: Errores de compilación Flutter

**Soluciones:**
1. Limpiar caché: `flutter clean`
2. Reinstalar dependencias: `flutter pub get`
3. Verificar versión de Flutter: `flutter doctor`

## 📁 Estructura del Proyecto

```
LibroSearch/
├── HOLASPRING6CV3/          # Backend Spring Boot
│   ├── docker-compose.yml   # Configuración Docker
│   └── src/                 # Código fuente backend
└── flut_librosearch/        # Frontend Flutter
    ├── lib/
    │   ├── core/            # Configuraciones y tema
    │   ├── data/            # Servicios y repositorios
    │   ├── presentation/    # Pantallas y widgets
    │   └── utils/           # Utilidades
    └── pubspec.yaml         # Dependencias Flutter
```

## 🤝 Video de la aplicación móvil

- Drive: https://drive.google.com/drive/folders/1fA9tqzwu3qnHgKmel-enF06YqaEVdwkD?usp=drive_link

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para la materia de Ingeniería de Software.

## 👤 Autor

**Luis Gerardo Herrera**
- GitHub: [@LuisGerH](https://github.com/LuisGerH)


---

