package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/perfil")
    public String mostrarPerfil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<UserModel> usuario = usuarioService.buscarPorUsername(userDetails.getUsername());

        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
        } else {
            return "redirect:/home?error";  // Redirigir si el usuario no existe
        }

        return "perfil";  // Retorna la vista perfil.html
    }

    @PostMapping("/perfil/actualizar")
public String actualizarPerfil(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam("nombre") String nombre,
        @RequestParam("email") String email,
        Model model) {

    boolean cambiado = usuarioService.actualizarPerfil(userDetails.getUsername(), nombre, email);

    if (cambiado) {
        // Si el nombre de usuario cambió, necesitamos una forma especial de manejar el logout
        if (!nombre.equals(userDetails.getUsername())) {
            // No redirigir directamente a /logout, sino a una página intermedia
            return "redirect:/perfil/logout";
        }
        return "redirect:/perfil?success";
    } else {
        return "redirect:/perfil?error";
    }
}
@GetMapping("/perfil/logout")
public String handleLogout(Model model) {
    // Esta página intermedia contiene el formulario de logout y lo envía automáticamente
    return "logout-redirect";
}
}
