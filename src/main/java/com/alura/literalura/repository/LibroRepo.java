package com.alura.literalura.repository;

import com.alura.literalura.libro.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepo extends JpaRepository<Libro,Long> {
    @Query(value = "select id_libro from libros where id_libro = ?1", nativeQuery = true)
    Optional<Long> buscar(Long id);

    @Query(value = "select * from libros", nativeQuery = true)
    List<Libro> mostrarLibros();

    @Query(value = "select * from libros where idioma like ?1", nativeQuery = true)
    List<Libro> libroPorIdioma(String idioma);
}
