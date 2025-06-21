import 'dart:convert';
import 'package:http/http.dart' as http;
import '../../core/constants/api_constants.dart';
import '../../utils/storage_helper.dart';

class AuthService {
  
  /// Prueba la conexión con el servidor
  Future<bool> testConnection() async {
    try {
      print('Probando conexión a: ${ApiConstants.baseUrl}');
      
      final response = await http.get(
        Uri.parse('${ApiConstants.baseUrl}/testConnection'),
        headers: ApiConstants.jsonHeaders,
      ).timeout(ApiConstants.connectionTimeout);
      
      print('Respuesta del servidor: ${response.statusCode}');
      
      return response.statusCode == 200;
    } catch (e) {
      print('Error de conexión: $e');
      return false;
    }
  }

  /// Realiza el proceso de login
  Future<AuthResult> login(String username, String password) async {
    try {
      print('Intentando login para usuario: $username');
      print('URL base: ${ApiConstants.baseUrl}');
      
      // Crear un cliente HTTP personalizado
      final client = http.Client();
      
      try {
        // Paso 1: Obtener la página de login para extraer CSRF token
        print('Paso 1: Obteniendo página de login...');
        final loginPageResponse = await client.get(
          Uri.parse('${ApiConstants.baseUrl}/login'),
          headers: {
            'User-Agent': 'Flutter-App/1.0',
            'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
          },
        ).timeout(ApiConstants.requestTimeout);
        
        print('Login page status: ${loginPageResponse.statusCode}');

        if (loginPageResponse.statusCode != 200) {
          return AuthResult.failure('No se puede acceder a la página de login');
        }

        // Extraer tokens y cookies
        String? csrfToken;
        String? jsessionid;

        // Buscar CSRF token en el HTML
        final csrfMatch = RegExp(r'name="_csrf"[^>]*value="([^"]*)"')
            .firstMatch(loginPageResponse.body);
        csrfToken = csrfMatch?.group(1);
        print('CSRF Token: $csrfToken');

        // Extraer JSESSIONID de las cookies
        final setCookieHeader = loginPageResponse.headers['set-cookie'];
        if (setCookieHeader != null) {
          final sessionMatch = RegExp(r'JSESSIONID=([^;]+)').firstMatch(setCookieHeader);
          jsessionid = sessionMatch?.group(1);
          print('JSESSIONID inicial: $jsessionid');
        }

        // Paso 2: Enviar las credenciales de login
        print('Paso 2: Enviando credenciales...');
        
        final headers = <String, String>{
          'Content-Type': 'application/x-www-form-urlencoded',
          'User-Agent': 'Flutter-App/1.0',
          'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
          'Referer': '${ApiConstants.baseUrl}/login',
        };

        if (jsessionid != null) {
          headers['Cookie'] = 'JSESSIONID=$jsessionid';
        }

        // Preparar el cuerpo de la petición
        final bodyData = <String, String>{
          'username': username,
          'password': password,
        };
        if (csrfToken != null) {
          bodyData['_csrf'] = csrfToken;
        }

        final encodedBody = bodyData.entries
            .map((e) => '${Uri.encodeComponent(e.key)}=${Uri.encodeComponent(e.value)}')
            .join('&');

        // Hacer la petición de login sin seguir redirecciones
        final request = http.Request('POST', Uri.parse('${ApiConstants.baseUrl}/procesar_login'));
        request.headers.addAll(headers);
        request.body = encodedBody;
        
        final streamedResponse = await client.send(request).timeout(ApiConstants.requestTimeout);
        final loginResponse = await http.Response.fromStream(streamedResponse);

        print('Login response status: ${loginResponse.statusCode}');
        print('Login response headers: ${loginResponse.headers}');

        // Analizar la respuesta
        if (loginResponse.statusCode == 302) {
          final location = loginResponse.headers['location'];
          print('Redirection location: $location');
          
          // Login exitoso - redirige a home
          if (location != null && (location.contains('/home') || location.endsWith('/home'))) {
            // Extraer las nuevas cookies de sesión
            final newSetCookie = loginResponse.headers['set-cookie'];
            if (newSetCookie != null) {
              await StorageHelper.saveSessionCookies(newSetCookie);
              await StorageHelper.saveUsername(username);
              print('Login exitoso - cookies guardadas');
              return AuthResult.success();
            } else {
              print('Login exitoso pero sin cookies');
              return AuthResult.failure('Error al obtener la sesión');
            }
          }
          // Login fallido - redirige a login con error
          else if (location != null && location.contains('/login')) {
            return AuthResult.failure('Usuario o contraseña incorrectos');
          }
        }
        
        // Si llegamos aquí, hubo un error
        return AuthResult.failure('Error en la autenticación (código: ${loginResponse.statusCode})');
        
      } finally {
        client.close();
      }
    } catch (e) {
      print('Error completo en login: $e');
      
      if (e.toString().contains('TimeoutException') || 
          e.toString().contains('Connection timed out')) {
        return AuthResult.failure(
          'Tiempo de espera agotado. Verifica que el servidor esté ejecutándose en ${ApiConstants.baseUrl}'
        );
      } else if (e.toString().contains('SocketException') ||
                 e.toString().contains('Connection refused')) {
        return AuthResult.failure(
          'No se puede conectar al servidor. Verifica que Spring Boot esté ejecutándose en ${ApiConstants.baseUrl}'
        );
      } else {
        return AuthResult.failure('Error de conexión: ${e.toString()}');
      }
    }
  }

  /// Verifica si la sesión actual es válida
  Future<bool> validateSession() async {
    try {
      final sessionCookies = await StorageHelper.getSessionCookies();
      if (sessionCookies == null || sessionCookies.isEmpty) {
        return false;
      }

      // Hacer una petición simple a un endpoint protegido
      final response = await http.get(
        Uri.parse('${ApiConstants.baseUrl}/user/api/validate-session'),
        headers: {
          'Cookie': sessionCookies,
          'Accept': 'application/json',
        },
      ).timeout(const Duration(seconds: 10));

      return response.statusCode == 200;
    } catch (e) {
      print('Error validando sesión: $e');
      return false;
    }
  }

  /// Cierra la sesión en el servidor
  Future<void> logout() async {
    try {
      final sessionCookies = await StorageHelper.getSessionCookies();
      if (sessionCookies != null) {
        await http.post(
          Uri.parse('${ApiConstants.baseUrl}/logout'),
          headers: {
            'Cookie': sessionCookies,
          },
        ).timeout(const Duration(seconds: 10));
      }
    } catch (e) {
      print('Error en logout del servidor: $e');
    } finally {
      // Limpiar datos locales independientemente del resultado
      await StorageHelper.clearAll();
    }
  }
}

/// Clase para encapsular el resultado de la autenticación
class AuthResult {
  final bool isSuccess;
  final String? errorMessage;

  AuthResult._({required this.isSuccess, this.errorMessage});

  factory AuthResult.success() => AuthResult._(isSuccess: true);
  
  factory AuthResult.failure(String message) => 
      AuthResult._(isSuccess: false, errorMessage: message);
}