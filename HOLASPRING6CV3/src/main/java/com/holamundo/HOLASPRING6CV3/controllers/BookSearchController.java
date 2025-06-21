package com.holamundo.HOLASPRING6CV3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true")
public class BookSearchController {

    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes";
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/user/search-books")
    public String searchBooks(Model model, Authentication authentication) {
        // Página web para búsqueda de libros (si necesitas una interfaz web)
        model.addAttribute("username", authentication.getName());
        return "home";
    }
    
    @GetMapping(value = "/user/api/search-books", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> searchBooksApi(
            @RequestParam String query, 
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response) {
        
        try {
            // Log detallado para debugging
            System.out.println("=== BOOK SEARCH REQUEST ===");
            System.out.println("Query: " + query);
            System.out.println("User authenticated: " + (authentication != null && authentication.isAuthenticated()));
            if (authentication != null) {
                System.out.println("Username: " + authentication.getName());
                System.out.println("Authorities: " + authentication.getAuthorities());
            }
            System.out.println("Session ID: " + request.getSession().getId());
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Remote Address: " + request.getRemoteAddr());
            System.out.println("User Agent: " + request.getHeader("User-Agent"));
            System.out.println("Accept Header: " + request.getHeader("Accept"));
            System.out.println("Cookie Header: " + request.getHeader("Cookie"));
            
            // Verificar autenticación
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("ERROR: Usuario no autenticado para búsqueda de libros");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Usuario no autenticado");
                errorResponse.put("code", "UNAUTHORIZED");
                errorResponse.put("message", "Debe iniciar sesión para buscar libros");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
            // Verificar parámetro query
            if (query == null || query.trim().isEmpty()) {
                System.out.println("ERROR: Query vacío");
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "El parámetro 'query' es requerido");
                errorResponse.put("code", "BAD_REQUEST");
                errorResponse.put("message", "Debe proporcionar un término de búsqueda");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Construir la URL para la API de Google Books
            String encodedQuery = java.net.URLEncoder.encode(query.trim(), "UTF-8");
            String googleBooksUrl = GOOGLE_BOOKS_API_URL + "?q=" + encodedQuery + "&maxResults=20&langRestrict=es";
            System.out.println("URL de Google Books: " + googleBooksUrl);
            
            // Realizar la petición a Google Books API
            ResponseEntity<Map> googleResponse = restTemplate.getForEntity(googleBooksUrl, Map.class);
            
            if (googleResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("Respuesta exitosa de Google Books API");
                Map<String, Object> responseBody = googleResponse.getBody();
                
                if (responseBody == null) {
                    System.out.println("ERROR: Respuesta vacía de Google Books API");
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Respuesta vacía del servicio de búsqueda");
                    errorResponse.put("code", "EMPTY_RESPONSE");
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
                }
                
                // Agregar información adicional a la respuesta
                Map<String, Object> enhancedResponse = new HashMap<>();
                enhancedResponse.put("items", responseBody.get("items"));
                enhancedResponse.put("totalItems", responseBody.get("totalItems"));
                enhancedResponse.put("query", query);
                enhancedResponse.put("timestamp", System.currentTimeMillis());
                enhancedResponse.put("user", authentication.getName());
                
                System.out.println("Búsqueda exitosa - " + 
                    (responseBody.get("totalItems") != null ? responseBody.get("totalItems") : "0") + 
                    " resultados encontrados");
                
                return ResponseEntity.ok(enhancedResponse);
            } else {
                System.out.println("ERROR: Respuesta no exitosa de Google Books API: " + googleResponse.getStatusCode());
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error al buscar libros en el servicio externo");
                errorResponse.put("code", "EXTERNAL_SERVICE_ERROR");
                errorResponse.put("statusCode", googleResponse.getStatusCode().value());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            }
            
        } catch (HttpClientErrorException e) {
            System.err.println("Error del cliente en Google Books API: " + e.getMessage());
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error en la búsqueda: Solicitud inválida");
            errorResponse.put("code", "CLIENT_ERROR");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (HttpServerErrorException e) {
            System.err.println("Error del servidor en Google Books API: " + e.getMessage());
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error del servidor de búsqueda externo");
            errorResponse.put("code", "EXTERNAL_SERVER_ERROR");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
            
        } catch (Exception e) {
            System.err.println("Error inesperado en búsqueda de libros: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("code", "INTERNAL_ERROR");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint para validar si la sesión está activa
     */
    @GetMapping(value = "/user/api/validate-session", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> validateSession(Authentication authentication, HttpServletRequest request) {
        System.out.println("=== SESSION VALIDATION ===");
        System.out.println("Session ID: " + request.getSession().getId());
        System.out.println("User authenticated: " + (authentication != null && authentication.isAuthenticated()));
        
        if (authentication != null && authentication.isAuthenticated()) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("user", authentication.getName());
            response.put("sessionId", request.getSession().getId());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("error", "Sesión no válida");
            errorResponse.put("code", "INVALID_SESSION");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @GetMapping(value = "/user/api/debug-session", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public ResponseEntity<?> debugSession(Authentication authentication, HttpServletRequest request) {
    Map<String, Object> debug = new HashMap<>();
    debug.put("authenticated", authentication != null && authentication.isAuthenticated());
    debug.put("username", authentication != null ? authentication.getName() : null);
    debug.put("sessionId", request.getSession().getId());
    debug.put("sessionCreationTime", request.getSession().getCreationTime());
    debug.put("sessionLastAccessed", request.getSession().getLastAccessedTime());
    debug.put("cookieHeader", request.getHeader("Cookie"));
    debug.put("userAgent", request.getHeader("User-Agent"));
    debug.put("remoteAddr", request.getRemoteAddr());
    
    return ResponseEntity.ok(debug);
}


    /**
     * Endpoint de prueba para verificar conectividad
     */
    @GetMapping(value = "/testConnection", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> testConnection() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Servidor funcionando correctamente");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }}