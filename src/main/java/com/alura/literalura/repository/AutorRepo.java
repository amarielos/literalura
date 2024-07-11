package com.alura.literalura.repository;

import com.alura.literalura.autor.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepo extends JpaRepository<Autor, Long> {

    @Query(value = "select * from autores where libro_id_libro = ?1", nativeQuery = true)
    List<Autor> mostrarAutoresPorLibro(Long idLibro);

    @Query(value = "select * from autores", nativeQuery = true)
    List<Autor> mostrarAutores();

    @Query(value = "select * from autores where nacimiento < ?1 and fallecimiento > ?1", nativeQuery = true)
    List<Autor> autoresVivosPorYear(int year);
}
