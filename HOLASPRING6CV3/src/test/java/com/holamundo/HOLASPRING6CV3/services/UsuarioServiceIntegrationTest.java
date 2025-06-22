package com.holamundo.HOLASPRING6CV3.services;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Sql("/data.sql")
public class UsuarioServiceIntegrationTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void testBuscarPorUsername_ExistingUser_ReturnsUser() {
        // When
        Optional<UserModel> result = usuarioService.buscarPorUsername("testuser");
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    public void testRegistrarUsuario_NewUser_ReturnsTrue() {
        // Given
        String username = "nuevouser";
        String email = "nuevo@test.com";
        String password = "password123";
        
        // When
        boolean result = usuarioService.registrarUsuario(username, email, password);
        
        // Then
        assertThat(result).isTrue();
        
        // Verificar que el usuario fue creado
        Optional<UserModel> created = usuarioService.buscarPorUsername(username);
        assertThat(created).isPresent();
        assertThat(created.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void testRegistrarUsuario_ExistingUsername_ReturnsFalse() {
        // Given - "testuser" ya existe en data.sql
        String username = "testuser";
        String email = "nuevo@test.com";
        String password = "password123";
        
        // When
        boolean result = usuarioService.registrarUsuario(username, email, password);
        
        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void testActualizarPerfil_ValidData_ReturnsSuccess() {
        // Given
        String currentUsername = "testuser";
        String newUsername = "testuser_updated";
        String newEmail = "updated@test.com";
        
        // When
        UsuarioService.ProfileUpdateResult result = usuarioService.actualizarPerfil(
            currentUsername, newUsername, newEmail);
        
        // Then
        assertThat(result).isEqualTo(UsuarioService.ProfileUpdateResult.SUCCESS);
        
        // Verificar que los datos fueron actualizados
        Optional<UserModel> updated = usuarioService.buscarPorUsername(newUsername);
        assertThat(updated).isPresent();
        assertThat(updated.get().getEmail()).isEqualTo(newEmail);
    }

    @Test
    public void testActualizarPerfil_EmailInUse_ReturnsEmailInUse() {
        // Given - "admin@test.com" ya existe
        String currentUsername = "testuser";
        String newUsername = "testuser";
        String existingEmail = "admin@test.com";
        
        // When
        UsuarioService.ProfileUpdateResult result = usuarioService.actualizarPerfil(
            currentUsername, newUsername, existingEmail);
        
        // Then
        assertThat(result).isEqualTo(UsuarioService.ProfileUpdateResult.EMAIL_IN_USE);
    }

    @Test
    public void testObtenerTodosUsuarios_ReturnsAllUsers() {
        // When
        List<UserModel> users = usuarioService.obtenerTodosUsuarios();
        
        // Then
        assertThat(users).hasSize(2); // testuser y testadmin
    }

    @Test
    public void testActualizarTema_ValidUser_ReturnsTrue() {
        // Given
        String username = "testuser";
        String newTheme = "dark";
        
        // When
        boolean result = usuarioService.actualizarTema(username, newTheme);
        
        // Then
        assertThat(result).isTrue();
        
        // Verificar que el tema fue actualizado
        Optional<UserModel> user = usuarioService.buscarPorUsername(username);
        assertThat(user).isPresent();
        assertThat(user.get().getTheme()).isEqualTo(newTheme);
    }

    @Test
    public void testCambiarPassword_ValidData_ReturnsSuccess() {
        // Given
        String username = "testuser";
        String currentPassword = "test123"; // La contraseña original
        String newPassword = "newPassword123";
        String confirmPassword = "newPassword123";
        
        // When
        UsuarioService.PasswordChangeResult result = usuarioService.cambiarPassword(
            username, currentPassword, newPassword, confirmPassword);
        
        // Then
        assertThat(result).isEqualTo(UsuarioService.PasswordChangeResult.SUCCESS);
    }

    @Test
    public void testCambiarPassword_WrongCurrentPassword_ReturnsError() {
        // Given
        String username = "testuser";
        String wrongCurrentPassword = "wrongpassword";
        String newPassword = "newPassword123";
        String confirmPassword = "newPassword123";
        
        // When
        UsuarioService.PasswordChangeResult result = usuarioService.cambiarPassword(
            username, wrongCurrentPassword, newPassword, confirmPassword);
        
        // Then
        assertThat(result).isEqualTo(UsuarioService.PasswordChangeResult.CURRENT_PASSWORD_INCORRECT);
    }
}