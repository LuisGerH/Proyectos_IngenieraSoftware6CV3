package com.holamundo.HOLASPRING6CV3;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class Holaspring6Cv3ApplicationTests {

    @Test
    void contextLoads() {
        // Esta prueba solo verifica que el contexto de Spring se carga sin errores
        // Si llega hasta aquí, significa que la configuración está bien
    }
}