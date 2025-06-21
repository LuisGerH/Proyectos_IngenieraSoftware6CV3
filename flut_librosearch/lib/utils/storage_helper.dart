import 'package:shared_preferences/shared_preferences.dart';

class StorageHelper {
  static const String _usernameKey = 'username';
  static const String _sessionCookiesKey = 'session_cookies';

  /// Guarda el nombre de usuario en el almacenamiento local
  static Future<void> saveUsername(String username) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_usernameKey, username);
    print('Username guardado: $username');
  }

  /// Obtiene el nombre de usuario del almacenamiento local
  static Future<String?> getUsername() async {
    final prefs = await SharedPreferences.getInstance();
    final username = prefs.getString(_usernameKey);
    print('Username obtenido: $username');
    return username;
  }

  /// Guarda las cookies de sesión completas
  static Future<void> saveSessionCookies(String cookies) async {
    final prefs = await SharedPreferences.getInstance();
    
    // Extraer solo las cookies necesarias
    String cleanCookies = _extractEssentialCookies(cookies);
    
    await prefs.setString(_sessionCookiesKey, cleanCookies);
    print('Cookies de sesión guardadas: $cleanCookies');
  }

  /// Extrae las cookies esenciales (JSESSIONID principalmente)
  static String _extractEssentialCookies(String cookies) {
    List<String> essentialCookies = [];
    
    // Dividir las cookies si hay múltiples
    List<String> cookieList = cookies.split(',');
    
    for (String cookie in cookieList) {
      cookie = cookie.trim();
      
      // Extraer JSESSIONID
      if (cookie.contains('JSESSIONID=')) {
        final jsessionMatch = RegExp(r'JSESSIONID=([^;]+)').firstMatch(cookie);
        if (jsessionMatch != null) {
          essentialCookies.add('JSESSIONID=${jsessionMatch.group(1)}');
        }
      }
      
      // Extraer otras cookies de sesión si es necesario
      if (cookie.contains('XSRF-TOKEN=')) {
        final xsrfMatch = RegExp(r'XSRF-TOKEN=([^;]+)').firstMatch(cookie);
        if (xsrfMatch != null) {
          essentialCookies.add('XSRF-TOKEN=${xsrfMatch.group(1)}');
        }
      }
    }
    
    return essentialCookies.join('; ');
  }

  /// Obtiene las cookies de sesión
  static Future<String?> getSessionCookies() async {
    final prefs = await SharedPreferences.getInstance();
    final cookies = prefs.getString(_sessionCookiesKey);
    print('Cookies de sesión obtenidas: $cookies');
    return cookies;
  }

  /// Limpia solo las cookies de sesión
  static Future<void> clearSessionCookies() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_sessionCookiesKey);
    print('Cookies de sesión eliminadas');
  }

  /// Limpia todos los datos del almacenamiento local
  static Future<void> clearAll() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
    print('Todos los datos del almacenamiento eliminados');
  }

  /// Verifica si el usuario está logueado
  static Future<bool> isLoggedIn() async {
    final username = await getUsername();
    final cookies = await getSessionCookies();
    final isLoggedIn = username != null && username.isNotEmpty && cookies != null && cookies.isNotEmpty;
    print('¿Está logueado? $isLoggedIn (username: $username, cookies: ${cookies != null})');
    return isLoggedIn;
  }

  /// Actualiza las cookies de sesión si vienen en una respuesta
  static Future<void> updateSessionCookiesFromResponse(Map<String, String> headers) async {
    final setCookie = headers['set-cookie'];
    if (setCookie != null && setCookie.isNotEmpty) {
      await saveSessionCookies(setCookie);
    }
  }

  /// Obtiene información de debug sobre el almacenamiento
  static Future<Map<String, dynamic>> getDebugInfo() async {
    final prefs = await SharedPreferences.getInstance();
    return {
      'username': prefs.getString(_usernameKey),
      'session_cookies': prefs.getString(_sessionCookiesKey),
      'all_keys': prefs.getKeys().toList(),
    };
  }
}