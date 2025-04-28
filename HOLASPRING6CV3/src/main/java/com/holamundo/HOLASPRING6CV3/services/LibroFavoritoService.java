package com.holamundo.HOLASPRING6CV3.services;

import com.holamundo.HOLASPRING6CV3.models.LibroFavorito;
import com.holamundo.HOLASPRING6CV3.models.UserModel;
import com.holamundo.HOLASPRING6CV3.repositories.LibroFavoritoRepository;
import com.holamundo.HOLASPRING6CV3.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroFavoritoService {

    @Autowired
    private LibroFavoritoRepository libroFavoritoRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Obtener todos los favoritos de un usuario
    public List<LibroFavorito> obtenerFavoritosPorUsuario(String username) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            return libroFavoritoRepository.findByUsuario(usuarioOpt.get());
        }
        return List.of(); // Lista vacía si no encuentra el usuario
    }
    
    // Agregar un libro a favoritos
    public boolean agregarFavorito(String username, String libroId, String titulo, String autor, String imagenUrl) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            
            // Verificar si ya existe en favoritos
            if (libroFavoritoRepository.existsByUsuarioAndLibroId(usuario, libroId)) {
                return false; // Ya existe, no se agrega
            }
            
            // Crear nueva entrada de favorito
            LibroFavorito favorito = new LibroFavorito();
            favorito.setUsuario(usuario);
            favorito.setLibroId(libroId);
            favorito.setTitulo(titulo);
            favorito.setAutor(autor);
            favorito.setImagenUrl(imagenUrl);
            
            libroFavoritoRepository.save(favorito);
            return true;
        }
        return false;
    }
    
    // Eliminar un favorito
    public boolean eliminarFavorito(String username, String libroId) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            Optional<LibroFavorito> favoritoOpt = libroFavoritoRepository.findByUsuarioAndLibroId(usuario, libroId);
            
            if (favoritoOpt.isPresent()) {
                libroFavoritoRepository.delete(favoritoOpt.get());
                return true;
            }
        }
        return false;
    }
    
    // Verificar si un libro es favorito
    public boolean esFavorito(String username, String libroId) {
        Optional<UserModel> usuarioOpt = userRepository.findByUsername(username);
        if (usuarioOpt.isPresent()) {
            UserModel usuario = usuarioOpt.get();
            return libroFavoritoRepository.existsByUsuarioAndLibroId(usuario, libroId);
        }
        return false;
    }
}