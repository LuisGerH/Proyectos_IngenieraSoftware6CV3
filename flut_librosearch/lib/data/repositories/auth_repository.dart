import '../services/auth_service.dart';
import '../../utils/storage_helper.dart';

class AuthRepository {
  final AuthService _authService = AuthService();

  /// Prueba la conexión con el servidor
  Future<bool> testConnection() async {
    return await _authService.testConnection();
  }

  /// Realiza el login del usuario
  Future<AuthResult> login(String username, String password) async {
    final result = await _authService.login(username, password);
    
    if (result.isSuccess) {
      // Guardar las credenciales si el login es exitoso
      await StorageHelper.saveUsername(username);
    }
    
    return result;
  }

  /// Cierra la sesión del usuario
  Future<void> logout() async {
    await StorageHelper.clearAll();
  }

  /// Verifica si el usuario está autenticado
  Future<bool> isAuthenticated() async {
    return await StorageHelper.isLoggedIn();
  }

  /// Obtiene el usuario actual
  Future<String?> getCurrentUser() async {
    return await StorageHelper.getUsername();
  }
}