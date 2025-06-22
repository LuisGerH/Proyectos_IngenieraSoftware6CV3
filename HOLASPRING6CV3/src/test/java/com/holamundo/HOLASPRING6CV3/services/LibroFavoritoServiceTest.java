package com.holamundo.HOLASPRING6CV3.services;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.repositories.LibroFavoritoRepository;
import com.holamundo.HOLASPRING6CV3.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibroFavoritoServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(LibroFavoritoServiceTest.class);

    @Mock
    private LibroFavoritoRepository libroFavoritoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LibroFavoritoService libroFavoritoService;

    private UserModel usuarioTest;

    @BeforeEach
    void setUp() {
        logger.info("=== CONFIGURACIÓN INICIAL DEL TEST ===");
        
        // Configurar datos de prueba
        usuarioTest = new UserModel();
        usuarioTest.setId(1L);
        usuarioTest.setUsername("testuser");
        usuarioTest.setEmail("test@test.com");
        
        logger.info("Usuario de prueba creado: ID={}, Username={}, Email={}", 
                   usuarioTest.getId(), usuarioTest.getUsername(), usuarioTest.getEmail());
        logger.info("=== CONFIGURACIÓN COMPLETADA ===\n");
    }

    @Test
    void esFavorito_CuandoUsuarioExisteYLibroEsFavorito_DeberiaRetornarTrue() {
        logger.info("🧪 INICIANDO TEST: esFavorito_CuandoUsuarioExisteYLibroEsFavorito_DeberiaRetornarTrue");
        
        // Arrange (Preparar)
        String username = "testuser";
        String libroId = "libro123";
        
        logger.info("📋 ARRANGE - Preparando datos del test:");
        logger.info("   Username: {}", username);
        logger.info("   LibroId: {}", libroId);

        // Simular que el usuario existe
        logger.info("🔧 Configurando mock: userRepository.findByUsername() retornará el usuario de prueba");
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(usuarioTest));

        // Simular que el libro SÍ es favorito
        logger.info("🔧 Configurando mock: libroFavoritoRepository.existsByUsuarioAndLibroId() retornará TRUE");
        when(libroFavoritoRepository.existsByUsuarioAndLibroId(usuarioTest, libroId))
                .thenReturn(true);

        logger.info("✅ ARRANGE completado - Mocks configurados");

        // Act (Actuar)
        logger.info("🚀 ACT - Ejecutando método esFavorito()...");
        boolean resultado = libroFavoritoService.esFavorito(username, libroId);
        logger.info("📤 ACT completado - Resultado obtenido: {}", resultado);

        // Assert (Verificar)
        logger.info("🔍 ASSERT - Verificando resultados:");
        assertTrue(resultado);
        logger.info("   ✅ Resultado es TRUE como se esperaba");
        
        // Verificar que se llamaron los métodos correctos
        logger.info("🔍 ASSERT - Verificando interacciones con mocks:");
        verify(userRepository).findByUsername(username);
        logger.info("   ✅ userRepository.findByUsername() fue llamado con: {}", username);
        
        verify(libroFavoritoRepository).existsByUsuarioAndLibroId(usuarioTest, libroId);
        logger.info("   ✅ libroFavoritoRepository.existsByUsuarioAndLibroId() fue llamado");
        
        logger.info("🎉 TEST COMPLETADO EXITOSAMENTE\n");
    }

    @Test
    void esFavorito_CuandoUsuarioExisteYLibroNoEsFavorito_DeberiaRetornarFalse() {
        logger.info("🧪 INICIANDO TEST: esFavorito_CuandoUsuarioExisteYLibroNoEsFavorito_DeberiaRetornarFalse");
        
        // Arrange
        String username = "testuser";
        String libroId = "libro123";
        
        logger.info("📋 ARRANGE - Preparando datos del test:");
        logger.info("   Username: {}", username);
        logger.info("   LibroId: {}", libroId);

        logger.info("🔧 Configurando mock: userRepository.findByUsername() retornará el usuario de prueba");
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(usuarioTest));

        // Simular que el libro NO es favorito
        logger.info("🔧 Configurando mock: libroFavoritoRepository.existsByUsuarioAndLibroId() retornará FALSE");
        when(libroFavoritoRepository.existsByUsuarioAndLibroId(usuarioTest, libroId))
                .thenReturn(false);

        logger.info("✅ ARRANGE completado - Mocks configurados");

        // Act
        logger.info("🚀 ACT - Ejecutando método esFavorito()...");
        boolean resultado = libroFavoritoService.esFavorito(username, libroId);
        logger.info("📤 ACT completado - Resultado obtenido: {}", resultado);

        // Assert
        logger.info("🔍 ASSERT - Verificando resultados:");
        assertFalse(resultado);
        logger.info("   ✅ Resultado es FALSE como se esperaba");
        
        logger.info("🔍 ASSERT - Verificando interacciones con mocks:");
        verify(userRepository).findByUsername(username);
        logger.info("   ✅ userRepository.findByUsername() fue llamado con: {}", username);
        
        verify(libroFavoritoRepository).existsByUsuarioAndLibroId(usuarioTest, libroId);
        logger.info("   ✅ libroFavoritoRepository.existsByUsuarioAndLibroId() fue llamado");
        
        logger.info("🎉 TEST COMPLETADO EXITOSAMENTE\n");
    }

    @Test
    void esFavorito_CuandoUsuarioNoExiste_DeberiaRetornarFalse() {
        logger.info("🧪 INICIANDO TEST: esFavorito_CuandoUsuarioNoExiste_DeberiaRetornarFalse");
        
        // Arrange
        String username = "usuarioInexistente";
        String libroId = "libro123";
        
        logger.info("📋 ARRANGE - Preparando datos del test:");
        logger.info("   Username: {} (este usuario NO existe)", username);
        logger.info("   LibroId: {}", libroId);

        // Simular que el usuario NO existe
        logger.info("🔧 Configurando mock: userRepository.findByUsername() retornará Optional.empty()");
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        logger.info("✅ ARRANGE completado - Mock configurado");

        // Act
        logger.info("🚀 ACT - Ejecutando método esFavorito()...");
        boolean resultado = libroFavoritoService.esFavorito(username, libroId);
        logger.info("📤 ACT completado - Resultado obtenido: {}", resultado);

        // Assert
        logger.info("🔍 ASSERT - Verificando resultados:");
        assertFalse(resultado);
        logger.info("   ✅ Resultado es FALSE como se esperaba (usuario no existe)");
        
        logger.info("🔍 ASSERT - Verificando interacciones con mocks:");
        verify(userRepository).findByUsername(username);
        logger.info("   ✅ userRepository.findByUsername() fue llamado con: {}", username);
        
        // Verificar que NO se llamó al repositorio de favoritos
        verify(libroFavoritoRepository, never()).existsByUsuarioAndLibroId(any(), any());
        logger.info("   ✅ libroFavoritoRepository.existsByUsuarioAndLibroId() NO fue llamado (como se esperaba)");
        
        logger.info("🎉 TEST COMPLETADO EXITOSAMENTE\n");
    }
}