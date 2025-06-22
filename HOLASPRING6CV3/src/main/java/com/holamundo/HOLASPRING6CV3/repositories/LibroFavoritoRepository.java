package com.holamundo.HOLASPRING6CV3.repositories;

import com.holamundo.HOLASPRING6CV3.models.LibroFavorito;
import com.holamundo.HOLASPRING6CV3.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LibroFavoritoRepository extends JpaRepository<LibroFavorito, Long> {
    List<LibroFavorito> findByUsuario(UserModel usuario);
    Optional<LibroFavorito> findByUsuarioAndLibroId(UserModel usuario, String libroId);
    boolean existsByUsuarioAndLibroId(UserModel usuario, String libroId);
    
}