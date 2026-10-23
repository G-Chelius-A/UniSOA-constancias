package com.api.universidad.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="libros")
public class Libro {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id_libro", nullable=false)
    private Long id;

    @Column(name="titulo", nullable=false, length=200)
    @NotBlank(message="El título no debe estar vacío")
    private String titulo;

    @Column(name="autor", nullable=false, length=150)
    @NotBlank(message="El autor no debe estar vacío")
    private String autor;

    @Column(name="isbn", unique=true, length=20)
    private String isbn;

    @Column(name="disponible", nullable=false)
    private Boolean disponible = true;

    //AL crear un libro, se indica que está disponible.

    @PrePersist
    public void onCreate() {
        if (disponible == null) {
            disponible = true;
        }
    }

    //Obtener ID y demás

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Boolean getDisponible() {
        return disponible != null && disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
