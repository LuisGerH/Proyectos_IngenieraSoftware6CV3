package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<UserModel> getUserByUsername(@PathVariable String username) {
        Optional<UserModel> user = usuarioService.buscarPorUsername(username);
        
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> userData) {
        Map<String, Object> response = new HashMap<>();
        
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");
        
        if (username == null || email == null || password == null) {
            response.put("success", false);
            response.put("message", "Faltan datos requeridos");
            return ResponseEntity.badRequest().body(response);
        }
        
        boolean success = usuarioService.registrarUsuario(username, email, password);
        
        if (success) {
            response.put("success", true);
            response.put("message", "Usuario registrado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Error al registrar usuario o usuario ya existe");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{username}/profile")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<Map<String, Object>> updateProfile(@PathVariable String username, 
                                                            @RequestBody Map<String, String> profileData) {
        Map<String, Object> response = new HashMap<>();
        
        String newUsername = profileData.get("username");
        String newEmail = profileData.get("email");
        
        if (newUsername == null || newEmail == null) {
            response.put("success", false);
            response.put("message", "Faltan datos requeridos");
            return ResponseEntity.badRequest().body(response);
        }
        
        UsuarioService.ProfileUpdateResult result = usuarioService.actualizarPerfil(username, newUsername, newEmail);
        
        switch (result) {
            case SUCCESS:
                response.put("success", true);
                response.put("message", "Perfil actualizado exitosamente");
                return ResponseEntity.ok(response);
            case EMAIL_IN_USE:
                response.put("success", false);
                response.put("message", "El email ya está en uso");
                return ResponseEntity.badRequest().body(response);
            case USERNAME_IN_USE:
                response.put("success", false);
                response.put("message", "El nombre de usuario ya está en uso");
                return ResponseEntity.badRequest().body(response);
            default:
                response.put("success", false);
                response.put("message", "Error al actualizar perfil");
                return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            usuarioService.eliminarUsuario(id);
            response.put("success", true);
            response.put("message", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar usuario");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}