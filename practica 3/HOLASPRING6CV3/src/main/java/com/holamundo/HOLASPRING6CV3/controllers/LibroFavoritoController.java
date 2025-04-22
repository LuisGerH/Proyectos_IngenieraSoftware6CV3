package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.models.LibroFavorito;
import com.holamundo.HOLASPRING6CV3.services.LibroFavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
public class LibroFavoritoController {

    @Autowired
    private LibroFavoritoService libroFavoritoService;
    
    // Obtener todos los favoritos del usuario actual
    @GetMapping
    public ResponseEntity<List<LibroFavorito>> obtenerFavoritos(Authentication authentication) {
        if (authentication != null) {
            List<LibroFavorito> favoritos = libroFavoritoService.obtenerFavoritosPorUsuario(authentication.getName());
            return ResponseEntity.ok(favoritos);
        }
        return ResponseEntity.status(401).build(); // No autorizado
    }
    
    // Verificar si un libro es favorito
    @GetMapping("/es-favorito/{libroId}")
    public ResponseEntity<Map<String, Boolean>> esFavorito(@PathVariable String libroId, Authentication authentication) {
        if (authentication != null) {
            boolean esFavorito = libroFavoritoService.esFavorito(authentication.getName(), libroId);
            Map<String, Boolean> response = new HashMap<>();
            response.put("esFavorito", esFavorito);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build();
    }
    
    // Agregar un libro a favoritos
    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarFavorito(
            @RequestBody Map<String, String> libroData,
            Authentication authentication) {
        
        if (authentication != null) {
            String libroId = libroData.get("libroId");
            String titulo = libroData.get("titulo");
            String autor = libroData.get("autor");
            String imagenUrl = libroData.get("imagenUrl");
            
            boolean agregado = libroFavoritoService.agregarFavorito(
                authentication.getName(), libroId, titulo, autor, imagenUrl);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", agregado);
            
            if (agregado) {
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "El libro ya está en favoritos");
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).build();
    }
    
    // Eliminar un libro de favoritos
    @DeleteMapping("/{libroId}")
    public ResponseEntity<Map<String, Object>> eliminarFavorito(
            @PathVariable String libroId,
            Authentication authentication) {
        
        if (authentication != null) {
            boolean eliminado = libroFavoritoService.eliminarFavorito(authentication.getName(), libroId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", eliminado);
            
            if (eliminado) {
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "El libro no está en favoritos");
                return ResponseEntity.status(404).body(response);
            }
        }
        return ResponseEntity.status(401).build();
    }
}