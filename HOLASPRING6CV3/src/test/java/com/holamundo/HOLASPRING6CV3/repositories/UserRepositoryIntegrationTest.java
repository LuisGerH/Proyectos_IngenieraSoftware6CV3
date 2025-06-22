package com.holamundo.HOLASPRING6CV3.repositories;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql("/data.sql") // Carga los datos de prueba
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByUsername_ExistingUser_ReturnsUser() {
        // Given - datos cargados por data.sql
        
        // When
        Optional<UserModel> result = userRepository.findByUsername("testuser");
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("test@test.com");
        assertThat(result.get().getRoles()).hasSize(1);
    }

    @Test
    public void testFindByEmail_ExistingEmail_ReturnsUser() {
        // When
        Optional<UserModel> result = userRepository.findByEmail("admin@test.com");
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testadmin");
        assertThat(result.get().getRoles()).hasSize(2); // ADMIN y USER
    }

    @Test
    public void testFindByUsername_NonExistingUser_ReturnsEmpty() {
        // When
        Optional<UserModel> result = userRepository.findByUsername("nonexistent");
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void testSaveUser_NewUser_SavesSuccessfully() {
        // Given
        UserModel newUser = new UserModel();
        newUser.setUsername("newuser");
        newUser.setEmail("new@test.com");
        newUser.setPassword("password123");
        
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        newUser.addRole(userRole);
        
        // When
        UserModel saved = userRepository.save(newUser);
        
        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        
        // Verificar que se guardó en la base de datos
        Optional<UserModel> found = userRepository.findByUsername("newuser");
        assertThat(found).isPresent();
    }

    @Test
    public void testFindAll_ReturnsAllUsers() {
        // When
        List<UserModel> users = userRepository.findAll();
        
        // Then
        assertThat(users).hasSize(2); // testuser y testadmin
    }

    @Test
    public void testDeleteUser_ExistingUser_DeletesSuccessfully() {
        // Given
        UserModel user = userRepository.findByUsername("testuser").orElseThrow();
        Long userId = user.getId();
        
        // When
        userRepository.deleteById(userId);
        
        // Then
        Optional<UserModel> deleted = userRepository.findById(userId);
        assertThat(deleted).isEmpty();
    }
}