import 'package:flutter/material.dart';
import 'core/theme/app_theme.dart';
import 'presentation/screens/auth/login_screen.dart';

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'LibroSearch',
      theme: AppTheme.lightTheme,
      home: LoginScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}