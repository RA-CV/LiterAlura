package com.example.literalura;

import com.example.literalura.client.GutendexClient;
import com.example.literalura.dto.Author;
import com.example.literalura.dto.Book;
import com.example.literalura.service.AuthorService;
import com.example.literalura.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    // Puedes inyectar AuthorService y BookService
    private final AuthorService authorService;
    private final BookService bookService;
    private final GutendexClient gutendexClient;


    public LiteraluraApplication(AuthorService authorService, BookService bookService, GutendexClient gutendexClient) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.gutendexClient = gutendexClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("""
                ********************************************************************************
                Bienvenido/a a LiterAlura =]

                Elija la opción a través de su número

                1) Buscar libro por título
                2) Listar libros registrados
                3) Listar Autores registrados
                4) Listar Autores Vivos en un determinado año
                5) Listar Libros por Idioma
                0) Salir

                Selecciona una opción:
                ********************************************************************************
                """);

            int opcion;
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // limpiar buffer

                switch (opcion) {
                    case 1:
                        System.out.print("Ingresa el título o parte del título del libro a buscar: ");
                        String tituloBusqueda = scanner.nextLine().trim();
                        if (tituloBusqueda.isEmpty()) {
                            System.out.println("El título no puede estar vacío.");
                            break;
                        }

                        try {
                            Book libroBuscado = gutendexClient.searchBookByTitle(tituloBusqueda);
                            if (libroBuscado == null) {
                                System.out.println("No se encontró ningún libro con ese título.");
                            } else {
                                bookService.saveBook(libroBuscado);
                                System.out.println("Libro encontrado y guardado en la base:");
                                System.out.println(libroBuscado);
                            }
                        } catch (Exception e) {
                            System.out.println("Error al consultar la API: " + e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Libros registrados:");
                        bookService.getAllBooks().forEach(System.out::println);
                        break;
                    case 3:
                        System.out.println("Autores registrados:");
                        authorService.getAllAuthors().forEach(System.out::println);
                        break;
                    case 4:
                        System.out.print("Ingresa un año válido (ej. 1900): ");
                        try {
                            int year = scanner.nextInt();
                            scanner.nextLine(); // limpiar buffer
                            if (year <= 0) {
                                System.out.println("Por favor ingresa un año positivo.");
                                break;
                            }
                            List<Author> autoresVivos = authorService.getAuthorsAliveInYear(year);
                            if (autoresVivos.isEmpty()) {
                                System.out.println("No se encontraron autores vivos en el año " + year);
                            } else {
                                System.out.println("Autores vivos en el año " + year + ":");
                                autoresVivos.forEach(System.out::println);
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Entrada inválida. Por favor ingresa un número entero para el año.");
                            scanner.nextLine(); // limpiar buffer
                        }
                        break;
                    case 5:
                        System.out.println("Consulta de cantidad de libros por idioma.");
                        System.out.println("Idiomas disponibles: 1 - Inglés (en), 2 - Español (es)");
                        System.out.print("Selecciona idioma (1 o 2): ");
                        int opcionIdioma = scanner.nextInt();
                        scanner.nextLine(); // limpiar buffer

                        String idioma;
                        switch (opcionIdioma) {
                            case 1:
                                idioma = "en";
                                break;
                            case 2:
                                idioma = "es";
                                break;
                            default:
                                idioma = null;
                        }

                        if (idioma != null) {
                            long cantidad = bookService.countBooksByLanguage(idioma);
                            System.out.printf("Cantidad de libros en idioma '%s': %d%n", idioma, cantidad);
                        } else {
                            System.out.println("Opción de idioma inválida.");
                        }
                        break;
                    case 0:
                        salir = true;
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción inválida. Intenta nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor ingresa un número entero para la opción.");
                scanner.nextLine(); // limpiar buffer
            }
        }
        scanner.close();
    }
}
