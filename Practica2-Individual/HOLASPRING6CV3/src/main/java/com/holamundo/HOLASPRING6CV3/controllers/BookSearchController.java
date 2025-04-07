package com.holamundo.HOLASPRING6CV3.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
public class BookSearchController {

    @GetMapping("/user/search-books")
    public String searchBooks(Model model, Authentication authentication) {
        // Puedes añadir atributos al modelo si es necesario
        model.addAttribute("username", authentication.getName());
        return "home"; // Esto busca una plantilla llamada search-books.html
    }
    
    // Si necesitas un endpoint para procesar las búsquedas desde el backend
    // (aunque en nuestro caso lo estamos haciendo con JavaScript directamente)
    @GetMapping("/user/api/search-books")
    public String processBookSearch(@RequestParam String query, Model model) {
        // Este método no se usa en la implementación actual pero podría ser útil
        // si decides mover la lógica de búsqueda al backend
        model.addAttribute("searchQuery", query);
        return "home";
    }
}