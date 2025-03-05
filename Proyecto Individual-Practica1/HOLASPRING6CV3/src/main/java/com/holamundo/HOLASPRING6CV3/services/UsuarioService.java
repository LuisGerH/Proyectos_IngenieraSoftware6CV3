package com.holamundo.HOLASPRING6CV3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.repositories.UserRepository;
import com.holamundo.HOLASPRING6CV3.repositories.RoleRepository;  // Inyectamos el repositorio de roles

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;  // Inyectamos el RoleRepository

    // Obtener usuario por nombre de usuario (para cargar perfil)
    public Optional<UserModel> buscarPorUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Actualizar perfil (nombre y email)
    public boolean actualizarPerfil(String username, String nuevoNombre, String nuevoEmail) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            usuario.setUsername(nuevoNombre);
            usuario.setEmail(nuevoEmail);
            userRepository.save(usuario);
            return true;
        }
        return false;
    }

    // Método de registro (sin cambios)
    public boolean registrarUsuario(String usuario, String email, String password) {
        try {
            if (userRepository.findByUsername(usuario).isPresent()) {
                return false;
            }

            UserModel nuevoUsuario = new UserModel();
            nuevoUsuario.setUsername(usuario);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(passwordEncoder.encode(password));

            roleRepository.findByName("ROLE_USER").ifPresent(nuevoUsuario::addRole);
            UserModel usuarioGuardado = userRepository.save(nuevoUsuario);

            return usuarioGuardado != null && usuarioGuardado.getId() != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserModel> obtenerTodosUsuarios() {
        return userRepository.findAll();
    }

    public void eliminarUsuario(Long id) {
        // Asegurarse de que el usuario exista antes de eliminar
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);  // Eliminar el usuario por ID
        } else {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }

    // Actualizar usuario desde la vista Admin
    public boolean actualizarUsuarioAdmin(Long id, String username, String email, String rol) {
        Optional<UserModel> usuarioOpt = userRepository.findById(id);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            usuario.setUsername(username);
            usuario.setEmail(email);
    
            // Limpiar los roles existentes y asignar el nuevo rol
            usuario.getRoles().clear();  // Limpiar roles anteriores
            
            // Asignar el rol según el valor pasado
            roleRepository.findByName(rol).ifPresent(usuario::addRole);

            // Guardar el usuario actualizado en la base de datos
            userRepository.save(usuario);
            return true;
        }
        return false;
    }
}
