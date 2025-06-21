class ApiConstants {
  // IP actualizada según tu ipconfig
  static const String baseUrl = 'http://192.168.100.43:8080';
  
  // Endpoints
  static const String loginEndpoint = '/login';
  static const String processLoginEndpoint = '/procesar_login';
  
  // Timeouts
  static const Duration requestTimeout = Duration(seconds: 15);
  static const Duration connectionTimeout = Duration(seconds: 10);
  
  // Headers
  static const Map<String, String> jsonHeaders = {
    'Content-Type': 'application/json',
  };
  
  static const Map<String, String> formHeaders = {
    'Content-Type': 'application/x-www-form-urlencoded',
  };
}