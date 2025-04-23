package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.services.RecomendacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class RecomendacionesController {

    @Autowired
    private RecomendacionesService recomendacionesService;
    
    /**
     * Ruta para mostrar la página de recomendaciones
     */
    @GetMapping("/user/recomendaciones")
    public String mostrarRecomendaciones(Model model, Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            List<Map<String, Object>> recomendaciones = recomendacionesService.obtenerRecomendaciones(username);
            
            model.addAttribute("username", username);
            model.addAttribute("recomendaciones", recomendaciones);
            model.addAttribute("cantidadRecomendaciones", recomendaciones.size());
            
            return "recomendaciones"; // Nombre de la plantilla de recomendaciones
        }
        
        return "redirect:/login";
    }
    
    /**
     * API para obtener recomendaciones en formato JSON
     */
    @GetMapping("/api/recomendaciones")
    @ResponseBody
    public List<Map<String, Object>> obtenerRecomendacionesAPI(Authentication authentication) {
        if (authentication != null) {
            return recomendacionesService.obtenerRecomendaciones(authentication.getName());
        }
        return List.of(); // Lista vacía si no hay autenticación
    }
}