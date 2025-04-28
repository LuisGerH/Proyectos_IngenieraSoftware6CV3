package com.holamundo.HOLASPRING6CV3.controllers;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.services.UsuarioService;
import com.holamundo.HOLASPRING6CV3.services.UsuarioService.AdminUpdateResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.holamundo.HOLASPRING6CV3.services.CustomUserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UsuarioService usuarioService;

    // Constructor para inyectar el servicio
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/gestion-usuarios")
    public String verUsuarios(Model model) {
        // Obtener el nombre de usuario logueado
        String username = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        // Obtener la lista de usuarios
        List<UserModel> usuarios = usuarioService.obtenerTodosUsuarios();

        // Filtrar para excluir el usuario logueado (tu usuario)
        List<UserModel> usuariosFiltrados = usuarios.stream()
                .filter(usuario -> !usuario.getUsername().equals(username))
                .collect(Collectors.toList());

        // Agregar la lista de usuarios filtrada al modelo
        model.addAttribute("usuarios", usuariosFiltrados);

        return "admin-gestion-usuarios";  // Vista que mostrará la lista sin tu usuario
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/eliminar-usuario")
    public String eliminarUsuario(@RequestParam("id") Long id) {
        usuarioService.eliminarUsuario(id);  // Llamar al servicio para eliminar el usuario
        return "redirect:/admin/gestion-usuarios?success";  // Redirigir con mensaje de éxito
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/actualizar-usuario")
    public String actualizarUsuario(@RequestParam("id") Long id,
                                    @RequestParam("username") String username,
                                    @RequestParam("email") String email,
                                    @RequestParam("rol") String rol,
                                    RedirectAttributes redirectAttributes) {

        // Actualizar el usuario en el servicio y manejar los distintos resultados
        AdminUpdateResult resultado = usuarioService.actualizarUsuarioAdmin(id, username, email, rol);
        
        // Redirigir según el resultado
        switch (resultado) {
            case SUCCESS:
                return "redirect:/admin/gestion-usuarios?success";
            case EMAIL_IN_USE:
                // Agregar el ID del usuario para mostrar el error en su formulario específico
                redirectAttributes.addAttribute("id", id);
                redirectAttributes.addAttribute("error", "emailInUse");
                return "redirect:/admin/gestion-usuarios";
            case USERNAME_IN_USE:
                redirectAttributes.addAttribute("id", id);
                redirectAttributes.addAttribute("error", "usernameInUse");
                return "redirect:/admin/gestion-usuarios";
            case USER_NOT_FOUND:
                redirectAttributes.addAttribute("id", id);
                redirectAttributes.addAttribute("error", "userNotFound");
                return "redirect:/admin/gestion-usuarios";
            default:
                redirectAttributes.addAttribute("id", id);
                redirectAttributes.addAttribute("error", "unknown");
                return "redirect:/admin/gestion-usuarios";
        }
    }
}