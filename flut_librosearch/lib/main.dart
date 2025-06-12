import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'LibroSearch',
      theme: ThemeData(
        primarySwatch: Colors.purple,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      home: LoginScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final _formKey = GlobalKey<FormState>();
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  bool _isLoading = false;
  String _errorMessage = '';

  // Cambia esta IP por la IP de tu máquina donde corre Spring Boot
  // Para emulador Android: usa 10.0.2.2
  // Para dispositivo físico: usa la IP de tu PC (ej: 192.168.1.100)
  final String baseUrl = 'http://10.0.2.2:8080'; // Cambiar según tu caso

  Future<void> _login() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _isLoading = true;
      _errorMessage = '';
    });

    try {
      // Primero obtenemos el token CSRF
      final csrfResponse = await http.get(
        Uri.parse('$baseUrl/login'),
        headers: {'Content-Type': 'application/json'},
      );

      String? csrfToken;
      String? jsessionid;

      // Extraer CSRF token de la respuesta HTML (método simple)
      if (csrfResponse.body.contains('_csrf')) {
        final csrfMatch = RegExp(r'name="_csrf"[^>]*value="([^"]*)"')
            .firstMatch(csrfResponse.body);
        csrfToken = csrfMatch?.group(1);
      }

      // Extraer JSESSIONID de las cookies
      final cookies = csrfResponse.headers['set-cookie'];
      if (cookies != null) {
        final sessionMatch = RegExp(r'JSESSIONID=([^;]+)').firstMatch(cookies);
        jsessionid = sessionMatch?.group(1);
      }

      // Realizar login
      final response = await http.post(
        Uri.parse('$baseUrl/procesar_login'),
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          if (jsessionid != null) 'Cookie': 'JSESSIONID=$jsessionid',
        },
        body: {
          'username': _usernameController.text,
          'password': _passwordController.text,
          if (csrfToken != null) '_csrf': csrfToken,
        },
      );

      if (response.statusCode == 200 || response.statusCode == 302) {
        // Login exitoso
        final prefs = await SharedPreferences.getInstance();
        await prefs.setString('username', _usernameController.text);
        
        // Navegar a la pantalla principal
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => HomeScreen()),
        );
      } else {
        setState(() {
          _errorMessage = 'Usuario o contraseña incorrectos';
        });
      }
    } catch (e) {
      setState(() {
        _errorMessage = 'Error de conexión: ${e.toString()}';
      });
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [
              Color(0xFF6A11CB),
              Color(0xFF2575FC),
            ],
          ),
        ),
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              padding: EdgeInsets.all(24.0),
              child: Card(
                elevation: 8,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Padding(
                  padding: EdgeInsets.all(32.0),
                  child: Form(
                    key: _formKey,
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Icon(
                          Icons.menu_book,
                          size: 64,
                          color: Color(0xFF6A11CB),
                        ),
                        SizedBox(height: 16),
                        ShaderMask(
                          shaderCallback: (bounds) => LinearGradient(
                            colors: [Color(0xFF6A11CB), Color(0xFF2575FC)],
                          ).createShader(bounds),
                          child: Text(
                            'LibroSearch',
                            style: TextStyle(
                              fontSize: 28,
                              fontWeight: FontWeight.bold,
                              color: Colors.white,
                            ),
                          ),
                        ),
                        SizedBox(height: 8),
                        Text(
                          'Bienvenido de nuevo',
                          style: TextStyle(
                            fontSize: 18,
                            color: Colors.grey[700],
                          ),
                        ),
                        SizedBox(height: 32),
                        
                        // Campo de usuario
                        TextFormField(
                          controller: _usernameController,
                          decoration: InputDecoration(
                            labelText: 'Usuario',
                            prefixIcon: Icon(Icons.person),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                              borderSide: BorderSide(color: Color(0xFF6A11CB)),
                            ),
                          ),
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Por favor ingresa tu usuario';
                            }
                            return null;
                          },
                        ),
                        SizedBox(height: 16),
                        
                        // Campo de contraseña
                        TextFormField(
                          controller: _passwordController,
                          obscureText: true,
                          decoration: InputDecoration(
                            labelText: 'Contraseña',
                            prefixIcon: Icon(Icons.lock),
                            border: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            focusedBorder: OutlineInputBorder(
                              borderRadius: BorderRadius.circular(12),
                              borderSide: BorderSide(color: Color(0xFF6A11CB)),
                            ),
                          ),
                          validator: (value) {
                            if (value == null || value.isEmpty) {
                              return 'Por favor ingresa tu contraseña';
                            }
                            return null;
                          },
                        ),
                        SizedBox(height: 24),
                        
                        // Mensaje de error
                        if (_errorMessage.isNotEmpty)
                          Container(
                            padding: EdgeInsets.all(12),
                            decoration: BoxDecoration(
                              color: Colors.red[50],
                              borderRadius: BorderRadius.circular(8),
                              border: Border.all(color: Colors.red[200]!),
                            ),
                            child: Row(
                              children: [
                                Icon(Icons.error, color: Colors.red, size: 20),
                                SizedBox(width: 8),
                                Expanded(
                                  child: Text(
                                    _errorMessage,
                                    style: TextStyle(color: Colors.red[700]),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        SizedBox(height: 16),
                        
                        // Botón de login
                        SizedBox(
                          width: double.infinity,
                          height: 50,
                          child: ElevatedButton(
                            onPressed: _isLoading ? null : _login,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Color(0xFF6A11CB),
                              foregroundColor: Colors.white,
                              shape: RoundedRectangleBorder(
                                borderRadius: BorderRadius.circular(12),
                              ),
                              elevation: 4,
                            ),
                            child: _isLoading
                                ? SizedBox(
                                    height: 20,
                                    width: 20,
                                    child: CircularProgressIndicator(
                                      color: Colors.white,
                                      strokeWidth: 2,
                                    ),
                                  )
                                : Row(
                                    mainAxisAlignment: MainAxisAlignment.center,
                                    children: [
                                      Icon(Icons.login),
                                      SizedBox(width: 8),
                                      Text(
                                        'Iniciar sesión',
                                        style: TextStyle(fontSize: 16),
                                      ),
                                    ],
                                  ),
                          ),
                        ),
                        SizedBox(height: 16),
                        
                        // Link de registro
                        TextButton(
                          onPressed: () {
                            // Navegar a registro (implementar después si necesitas)
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text('Registro no implementado')),
                            );
                          },
                          child: Text(
                            '¿No tienes cuenta? Regístrate',
                            style: TextStyle(color: Color(0xFF6A11CB)),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  @override
  void dispose() {
    _usernameController.dispose();
    _passwordController.dispose();
    super.dispose();
  }
}

// Pantalla de inicio básica
class HomeScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('LibroSearch'),
        backgroundColor: Color(0xFF6A11CB),
        foregroundColor: Colors.white,
        actions: [
          IconButton(
            icon: Icon(Icons.logout),
            onPressed: () async {
              final prefs = await SharedPreferences.getInstance();
              await prefs.clear();
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(builder: (context) => LoginScreen()),
              );
            },
          ),
        ],
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.check_circle,
              color: Colors.green,
              size: 80,
            ),
            SizedBox(height: 20),
            Text(
              '¡Login exitoso!',
              style: TextStyle(
                fontSize: 24,
                fontWeight: FontWeight.bold,
                color: Color(0xFF6A11CB),
              ),
            ),
            SizedBox(height: 10),
            Text(
              'Bienvenido a LibroSearch',
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey[600],
              ),
            ),
          ],
        ),
      ),
    );
  }
}