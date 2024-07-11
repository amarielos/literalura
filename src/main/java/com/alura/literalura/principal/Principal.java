package com.alura.literalura.principal;

import com.alura.literalura.autor.Autor;
import com.alura.literalura.libro.DatosLibro;
import com.alura.literalura.libro.Libro;
import com.alura.literalura.repository.AutorRepo;
import com.alura.literalura.repository.LibroRepo;
import com.alura.literalura.service.DatosAPI;
import com.alura.literalura.service.ConsumoAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private final String URL_BASE ="http://gutendex.com/books/";
    private final String URL_BUSCAR ="?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private Scanner teclado = new Scanner(System.in);
    private ObjectMapper conversor = new ObjectMapper();
    private LibroRepo libroRepository;
    private AutorRepo autorRepository;

    public Principal(LibroRepo libroRepository, AutorRepo autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void mostrarMenu() throws JsonProcessingException {
        int opcion=-1;
        while ( opcion!=0) {
            var menu = """
                    Elija la opción a través de su número: 
                    1 - Buscar libro por titulo 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma                                  
                    0 - Salir
                    """;
            try{
                System.out.println(menu);

                opcion = teclado.nextInt();
                teclado.nextLine();
                switch (opcion) {
                    case 1:
                        getLibroPorTitulo();
                        break;
                    case 2:
                        mostrarLibrosRegistrados();
                        break;
                    case 3:
                        mostrarAutoresRegistrados();
                        break;
                    case 4:
                        mostrarAutoresVivosPorYear();
                        break;
                    case 5:
                        mostrarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicación...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            }catch (Exception e){
                System.out.println("Número no válido");
                teclado.next();
            }


        }
    }

    private void getLibroPorTitulo() throws JsonProcessingException {
        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE +URL_BUSCAR+ nombreLibro.toLowerCase().replace(" ","%20"));
        DatosAPI datos = conversor.readValue(json, DatosAPI.class);
        if (datos.resultados().size()==0){
            System.out.println("Ningun libro coincide con la búsqueda.");
        }else{
            DatosLibro libroAux = datos.resultados().getFirst();
            if (libroRepository.buscar(libroAux.id()).isEmpty()){
                Libro libro = new Libro(libroAux);
                List<Autor> autores = libroAux.autores().stream()
                        .map(Autor::new)
                        .collect(Collectors.toList());

                libro.setAutores(autores);
                autores.forEach(a -> a.setLibro(libro));
                System.out.println(libro);
                libroRepository.save(libro);
                autorRepository.saveAll(autores);
            }
        }
    }

    private void mostrarLibrosRegistrados() {
        var libros = libroRepository.mostrarLibros();
        libros.forEach(l -> l.setAutores(autorRepository.mostrarAutoresPorLibro(l.getIdLibro())));
        System.out.println(libros);
    }

    private void mostrarAutoresRegistrados() {
        var autores = autorRepository.mostrarAutores();
        System.out.println(autores);
    }

    private void mostrarAutoresVivosPorYear() {
        try {
            System.out.println("Escriba el año: ");
            int year = teclado.nextInt();
            var autores = autorRepository.autoresVivosPorYear(year);
            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos durante ese año registrados");
            }else{
                System.out.println(autores);
            }
        } catch (Exception e) {
            System.out.println("Ingrese un año válido");
        }

    }
    private void mostrarLibrosPorIdioma() {
        System.out.printf("""
            Escriba el idioma a buscar:
            en - inglés
            es - español
            fr - frances
            pt - portugués
            """);
        String idioma = teclado.next().toLowerCase();
        var libros = libroRepository.libroPorIdioma(idioma);
        if(libros.isEmpty()){
            System.out.println("No se encontraron libros en ese idioma");
        }else{
            libros.forEach(l -> l.setAutores(autorRepository.mostrarAutoresPorLibro(l.getIdLibro())));
            System.out.println(libros);
        }
    }
}
