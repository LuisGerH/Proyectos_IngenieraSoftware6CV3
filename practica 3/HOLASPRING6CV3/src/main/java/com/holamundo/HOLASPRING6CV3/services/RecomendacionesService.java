package com.holamundo.HOLASPRING6CV3.services;

import com.holamundo.HOLASPRING6CV3.models.LibroFavorito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacionesService {

    @Autowired
    private LibroFavoritoService libroFavoritoService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_BASE_URL = "https://openlibrary.org/search.json";
    
    /**
     * Obtiene recomendaciones basadas en los autores y temas de los libros favoritos
     * @param username Nombre de usuario
     * @return Lista de mapas con información de libros recomendados
     */
    public List<Map<String, Object>> obtenerRecomendaciones(String username) {
        List<LibroFavorito> favoritos = libroFavoritoService.obtenerFavoritosPorUsuario(username);
        
        if (favoritos.isEmpty()) {
            return new ArrayList<>(); // No hay favoritos, no podemos recomendar
        }
        
        // Extraer autores de los favoritos
        Set<String> autores = favoritos.stream()
                .filter(libro -> libro.getAutor() != null && !libro.getAutor().isEmpty())
                .map(LibroFavorito::getAutor)
                .collect(Collectors.toSet());
        
        // Si hay autores, buscar libros por estos autores
        List<Map<String, Object>> recomendaciones = new ArrayList<>();
        
        for (String autor : autores) {
            try {
                // Límite de 3 libros por autor para evitar demasiadas recomendaciones
                String url = API_BASE_URL + "?author=" + autor.replace(" ", "+") + "&limit=3";
                Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                
                if (response != null && response.containsKey("docs")) {
                    List<Map<String, Object>> docs = (List<Map<String, Object>>) response.get("docs");
                    
                    for (Map<String, Object> doc : docs) {
                        // Verificar que no sea un libro que ya está en favoritos
                        String key = (String) doc.get("key");
                        if (key != null && !estaEnFavoritos(favoritos, key)) {
                            Map<String, Object> libro = new HashMap<>();
                            libro.put("id", key);
                            libro.put("titulo", doc.get("title"));
                            libro.put("autor", autor);
                            
                            // Construir URL de imagen si hay cover_i
                            if (doc.containsKey("cover_i")) {
                                Integer coverId = (Integer) doc.get("cover_i");
                                libro.put("imagenUrl", "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                            } else {
                                libro.put("imagenUrl", "/images/no-cover.jpg"); // Imagen por defecto
                            }
                            
                            recomendaciones.add(libro);
                        }
                    }
                }
            } catch (Exception e) {
                // Logging error pero continuamos con el siguiente autor
                System.err.println("Error al obtener recomendaciones para el autor " + autor + ": " + e.getMessage());
            }
        }
        
        // Limitar a 10 recomendaciones como máximo
        return recomendaciones.stream()
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * Verifica si un libro ya está en la lista de favoritos
     */
    private boolean estaEnFavoritos(List<LibroFavorito> favoritos, String libroId) {
        return favoritos.stream()
                .anyMatch(fav -> libroId.equals(fav.getLibroId()));
    }
}