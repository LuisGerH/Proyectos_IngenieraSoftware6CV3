import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flut_librosearch/main.dart';
import 'package:flut_librosearch/app.dart';

void main() {
  group('LibroSearch App Tests', () {
    testWidgets('App launches and shows login screen', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Verify that we're on the login screen
      expect(find.text('LibroSearch'), findsOneWidget);
      expect(find.text('Bienvenido de nuevo'), findsOneWidget);
      expect(find.text('Usuario'), findsOneWidget);
      expect(find.text('Contraseña'), findsOneWidget);
      expect(find.text('Iniciar sesión'), findsOneWidget);
    });

    testWidgets('Login form validation works', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Try to login without entering credentials
      await tester.tap(find.text('Iniciar sesión'));
      await tester.pump();

      // Verify validation messages appear
      expect(find.text('Por favor ingresa tu usuario'), findsOneWidget);
      expect(find.text('Por favor ingresa tu contraseña'), findsOneWidget);
    });

    testWidgets('User can enter credentials', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Find the text fields
      final usernameField = find.widgetWithText(TextFormField, 'Usuario');
      final passwordField = find.widgetWithText(TextFormField, 'Contraseña');

      // Enter text into the fields
      await tester.enterText(usernameField, 'testuser');
      await tester.enterText(passwordField, 'testpass');

      // Verify text was entered
      expect(find.text('testuser'), findsOneWidget);
      // Note: Password field text won't be visible due to obscureText: true
    });

    testWidgets('Test connection button is present', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Verify test connection button exists
      expect(find.text('Probar conexión'), findsOneWidget);
      expect(find.byIcon(Icons.wifi_tethering), findsOneWidget);
    });

    testWidgets('Register link is present', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Verify register link exists
      expect(find.text('¿No tienes cuenta? Regístrate'), findsOneWidget);
    });

    testWidgets('App shows server info', (WidgetTester tester) async {
      // Build our app and trigger a frame
      await tester.pumpWidget(MyApp());

      // Verify server info is displayed
      expect(find.textContaining('Servidor:'), findsOneWidget);
      expect(find.textContaining('192.168.100.43:8080'), findsOneWidget);
    });
  });
}