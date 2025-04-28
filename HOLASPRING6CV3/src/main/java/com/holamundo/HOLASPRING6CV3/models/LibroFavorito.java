package com.holamundo.HOLASPRING6CV3.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "libros_favoritos")
public class LibroFavorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserModel usuario;
    
    @Column(name = "libro_id", nullable = false)
    private String libroId;
    
    @Column(name = "titulo", nullable = false)
    private String titulo;
    
    @Column(name = "autor")
    private String autor;
    
    @Column(name = "imagen_url")
    private String imagenUrl;
    
    @Column(name = "fecha_guardado")
    private LocalDateTime fechaGuardado;

    // Constructor por defecto
    public LibroFavorito() {
        this.fechaGuardado = LocalDateTime.now();
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUsuario() {
        return usuario;
    }

    public void setUsuario(UserModel usuario) {
        this.usuario = usuario;
    }

    public String getLibroId() {
        return libroId;
    }

    public void setLibroId(String libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public LocalDateTime getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDateTime fechaGuardado) {
        this.fechaGuardado = fechaGuardado;
    }
}