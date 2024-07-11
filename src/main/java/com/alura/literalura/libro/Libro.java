package com.alura.literalura.libro;

import com.alura.literalura.autor.Autor;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    private long idLibro;
    private String titulo;

    @OneToMany(mappedBy = "idAutor", fetch = FetchType.EAGER)
    private List<Autor> autores;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "descargas")
    private int numDescargas;


    public Libro(DatosLibro libro){
        this.idLibro = libro.id();
        this.titulo = libro.titulo();
        this.idioma = libro.idiomas().getFirst();
        this.numDescargas = libro.descargas();
    }

    @Override
    public String toString() {
        return "\nTitulo: " + titulo + "\nAutores=" + autores.getFirst().getNombre() + "\nIdioma: " + idioma
                + "\nDescargas: " + numDescargas + "\n";
    }


}
