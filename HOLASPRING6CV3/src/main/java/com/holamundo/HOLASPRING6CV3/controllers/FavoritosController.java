package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.models.LibroFavorito;
import com.holamundo.HOLASPRING6CV3.services.LibroFavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class FavoritosController {

    @Autowired
    private LibroFavoritoService libroFavoritoService;

    @GetMapping("/user/mis-favoritos")
    public String mostrarFavoritos(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            List<LibroFavorito> favoritos = libroFavoritoService.obtenerFavoritosPorUsuario(username);
            
            model.addAttribute("favoritos", favoritos);
            model.addAttribute("username", username);
            model.addAttribute("cantidadFavoritos", favoritos.size());
            
            return "favoritos"; // Esta será nuestra nueva plantilla
        }
        
        return "redirect:/login";
    }
}