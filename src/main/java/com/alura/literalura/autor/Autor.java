package com.alura.literalura.autor;

import com.alura.literalura.libro.Libro;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private long idAutor;
    private String nombre;
    private int nacimiento;
    private int fallecimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "idLibro")
    private Libro libro;

    public Autor(DatosAutor autor){
        this.nombre = autor.nombre();
        this.nacimiento = autor.nacimiento();
        this.fallecimiento = autor.fallecimiento();
    }

    @Override
    public String toString() {
        return String.format("\nNombre: %s \nFecha de naciemiento: %s \nFecha de falleciemiento: %s \n ",nombre,nacimiento,fallecimiento);
    }

}