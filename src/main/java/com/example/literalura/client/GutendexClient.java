package com.example.literalura.client;

import com.example.literalura.dto.Author;
import com.example.literalura.dto.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class GutendexClient {

    private static final String BASE_URL = "https://gutendex.com/books/?search=";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GutendexClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Busca un libro por título en la API Gutendex y retorna el primer libro encontrado.
     * @param title título o parte del título a buscar
     * @return objeto Book mapeado o null si no se encontró
     * @throws Exception en caso de error de conexión o parsing
     */

    public Book searchBookByTitle(String title) throws Exception {
        String url = BASE_URL + title.replace(" ", "+");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            System.err.println("Error en la API Gutendex: código " + response.statusCode());
            return null;
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode results = root.get("results");

        if (results == null || !results.isArray() || results.size() == 0) {
            return null; // No hay resultados
        }

        JsonNode firstBook = results.get(0);

        // Extraemos datos
        String bookTitle = firstBook.get("title").asText();

        // Extraemos primer idioma
        String language = null;
        JsonNode languagesNode = firstBook.get("languages");
        if (languagesNode != null && languagesNode.isArray() && languagesNode.size() > 0) {
            language = languagesNode.get(0).asText();
        }

        // Descargas
        Integer downloadCount = null;
        if (firstBook.has("download_count")) {
            downloadCount = firstBook.get("download_count").asInt();
        }

        // Autor (solo primer autor)
        Author author = null;
        JsonNode authorsNode = firstBook.get("authors");
        if (authorsNode != null && authorsNode.isArray() && authorsNode.size() > 0) {
            JsonNode firstAuthor = authorsNode.get(0);
            String authorName = firstAuthor.get("name").asText();
            Integer birthYear = firstAuthor.hasNonNull("birth_year") ? firstAuthor.get("birth_year").asInt() : null;
            Integer deathYear = firstAuthor.hasNonNull("death_year") ? firstAuthor.get("death_year").asInt() : null;

            author = new Author(authorName, birthYear, deathYear);
        }

        // Construimos el libro con el autor
        Book book = new Book();
        book.setTitle(bookTitle);
        book.setLanguage(language);
        book.setDownloadCount(downloadCount);
        book.setAuthor(author);

        return book;
    }
}