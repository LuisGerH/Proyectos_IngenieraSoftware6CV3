package com.holamundo.HOLASPRING6CV3.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@Sql("/data.sql")
public class UserRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    // ================ PRUEBAS GET ================
    
    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllUsers_AsAdmin_ReturnsAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").exists())
                .andExpect(jsonPath("$[0].email").exists());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllUsers_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetUserByUsername_OwnProfile_ReturnsUser() throws Exception {
        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testGetUserByUsername_OtherProfile_ReturnsForbidden() throws Exception {
        mockMvc.perform(get("/api/users/testadmin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUserByUsername_AsAdmin_ReturnsUser() throws Exception {
        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetUserByUsername_NonExistent_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/users/nonexistent"))
                .andExpect(status().isNotFound());
    }

    // ================ PRUEBAS POST (REGISTRO) ================

    @Test
    public void testRegisterUser_ValidData_ReturnsCreated() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("username", "newuser");
        userData.put("email", "newuser@test.com");
        userData.put("password", "password123");

        String jsonRequest = objectMapper.writeValueAsString(userData);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario registrado exitosamente"));

        // Verificar que el usuario fue creado en la base de datos
        Optional<UserModel> created = userRepository.findByUsername("newuser");
        assertThat(created).isPresent();
        assertThat(created.get().getEmail()).isEqualTo("newuser@test.com");
    }

    @Test
    public void testRegisterUser_ExistingUsername_ReturnsBadRequest() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("username", "testuser"); // Ya existe
        userData.put("email", "new@test.com");
        userData.put("password", "password123");

        String jsonRequest = objectMapper.writeValueAsString(userData);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Error al registrar usuario o usuario ya existe"));
    }

    @Test
    public void testRegisterUser_MissingData_ReturnsBadRequest() throws Exception {
        Map<String, String> userData = new HashMap<>();
        userData.put("username", "newuser");
        // Falta email y password

        String jsonRequest = objectMapper.writeValueAsString(userData);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Faltan datos requeridos"));
    }

    // ================ PRUEBAS PUT (ACTUALIZAR PERFIL) ================

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testUpdateProfile_ValidData_ReturnsSuccess() throws Exception {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("username", "testuser_updated");
        profileData.put("email", "updated@test.com");

        String jsonRequest = objectMapper.writeValueAsString(profileData);

        mockMvc.perform(put("/api/users/testuser/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Perfil actualizado exitosamente"));

        // Verificar que los datos fueron actualizados
        Optional<UserModel> updated = userRepository.findByUsername("testuser_updated");
        assertThat(updated).isPresent();
        assertThat(updated.get().getEmail()).isEqualTo("updated@test.com");
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testUpdateProfile_EmailInUse_ReturnsBadRequest() throws Exception {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("username", "testuser");
        profileData.put("email", "admin@test.com"); // Email ya existe

        String jsonRequest = objectMapper.writeValueAsString(profileData);

        mockMvc.perform(put("/api/users/testuser/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("El email ya está en uso"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    public void testUpdateProfile_OtherUserProfile_ReturnsForbidden() throws Exception {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("username", "testadmin_updated");
        profileData.put("email", "newemail@test.com");

        String jsonRequest = objectMapper.writeValueAsString(profileData);

        mockMvc.perform(put("/api/users/testadmin/profile") // Intentando actualizar otro usuario
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProfile_AsAdmin_ReturnsSuccess() throws Exception {
        Map<String, String> profileData = new HashMap<>();
        profileData.put("username", "testuser_by_admin");
        profileData.put("email", "admin_updated@test.com");

        String jsonRequest = objectMapper.writeValueAsString(profileData);

        mockMvc.perform(put("/api/users/testuser/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ================ PRUEBAS DELETE ================

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser_ValidId_ReturnsSuccess() throws Exception {
        // Obtener un usuario existente
        UserModel user = userRepository.findByUsername("testuser").orElseThrow();
        Long userId = user.getId();

        mockMvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Usuario eliminado exitosamente"));

        // Verificar que el usuario fue eliminado
        Optional<UserModel> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteUser_NonExistentId_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_AsUser_ReturnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    // ================ PRUEBAS SIN AUTENTICACIÓN ================

    @Test
    public void testGetAllUsers_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserByUsername_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/testuser"))
                .andExpect(status().isUnauthorized());
    }
}