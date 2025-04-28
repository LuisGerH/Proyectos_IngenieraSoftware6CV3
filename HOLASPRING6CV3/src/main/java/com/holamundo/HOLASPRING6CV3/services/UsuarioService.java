package com.holamundo.HOLASPRING6CV3.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.repositories.UserRepository;
import com.holamundo.HOLASPRING6CV3.repositories.RoleRepository;

import java.util.Optional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // Obtener usuario por nombre de usuario (para cargar perfil)
    public Optional<UserModel> buscarPorUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Actualizar perfil (nombre y email)
    public enum ProfileUpdateResult {
        SUCCESS,
        EMAIL_IN_USE,
        USERNAME_IN_USE,
        USER_NOT_FOUND,
        ERROR
    }

    public ProfileUpdateResult actualizarPerfil(String username, String nuevoNombre, String nuevoEmail) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            
            // Verificar si el nombre de usuario ya está en uso por otro usuario
            if (!nuevoNombre.equals(username)) {
                Optional<UserModel> nombreExistente = userRepository.findByUsername(nuevoNombre);
                if (nombreExistente.isPresent()) {
                    return ProfileUpdateResult.USERNAME_IN_USE;
                }
            }
            
            // Verificar si el email ya está en uso por otro usuario
            Optional<UserModel> emailExistente = userRepository.findByEmail(nuevoEmail);
            if (emailExistente.isPresent() && !emailExistente.get().getId().equals(usuario.getId())) {
                return ProfileUpdateResult.EMAIL_IN_USE;
            }
            
            try {
                usuario.setUsername(nuevoNombre);
                usuario.setEmail(nuevoEmail);
                userRepository.save(usuario);
                return ProfileUpdateResult.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return ProfileUpdateResult.ERROR;
            }
        }
        return ProfileUpdateResult.USER_NOT_FOUND;
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

    // Resultados posibles al actualizar un usuario desde el admin
    public enum AdminUpdateResult {
        SUCCESS,
        EMAIL_IN_USE,
        USERNAME_IN_USE,
        USER_NOT_FOUND,
        ERROR
    }

    // Actualizar usuario desde la vista Admin con validaciones
    public AdminUpdateResult actualizarUsuarioAdmin(Long id, String username, String email, String rol) {
        Optional<UserModel> usuarioOpt = userRepository.findById(id);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            String nombreOriginal = usuario.getUsername();
            String emailOriginal = usuario.getEmail();
            
            // Verificar si el nombre de usuario ya está en uso por otro usuario
            if (!username.equals(nombreOriginal)) {
                Optional<UserModel> nombreExistente = userRepository.findByUsername(username);
                if (nombreExistente.isPresent() && !nombreExistente.get().getId().equals(id)) {
                    return AdminUpdateResult.USERNAME_IN_USE;
                }
            }
            
            // Verificar si el email ya está en uso por otro usuario
            if (!email.equals(emailOriginal)) {
                Optional<UserModel> emailExistente = userRepository.findByEmail(email);
                if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
                    return AdminUpdateResult.EMAIL_IN_USE;
                }
            }
            
            try {
                usuario.setUsername(username);
                usuario.setEmail(email);
        
                // Limpiar los roles existentes y asignar el nuevo rol
                usuario.getRoles().clear();  // Limpiar roles anteriores
                
                // Asignar el rol según el valor pasado
                roleRepository.findByName(rol).ifPresent(usuario::addRole);

                // Guardar el usuario actualizado en la base de datos
                userRepository.save(usuario);
                return AdminUpdateResult.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                return AdminUpdateResult.ERROR;
            }
        }
        return AdminUpdateResult.USER_NOT_FOUND;
    }

    public boolean actualizarTema(String username, String tema) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            usuario.setTheme(tema);
            userRepository.save(usuario);
            return true;
        }
        return false;
    }
    
    public enum PasswordChangeResult {
        SUCCESS, 
        CURRENT_PASSWORD_INCORRECT, 
        PASSWORDS_DONT_MATCH,
        ERROR
    }
    
    public PasswordChangeResult cambiarPassword(String username, String currentPassword, 
                                              String newPassword, String confirmPassword) {
        // Verificar que las contraseñas nuevas coincidan
        if (!newPassword.equals(confirmPassword)) {
            return PasswordChangeResult.PASSWORDS_DONT_MATCH;
        }
        
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            
            // Verificar que la contraseña actual sea correcta
            if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
                return PasswordChangeResult.CURRENT_PASSWORD_INCORRECT;
            }
            
            // Cambiar la contraseña
            usuario.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(usuario);
            return PasswordChangeResult.SUCCESS;
        }
        
        return PasswordChangeResult.ERROR;
    }
}