package com.example.literalura.service;

import com.example.literalura.dto.Book;
import com.example.literalura.repository.BookRepository;
import com.example.literalura.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findBooksByLanguage(String language) {
        return bookRepository.findByLanguage(language);
    }

    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public long countBooksByLanguage(String language) {
        return bookRepository.countByLanguage(language);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        // Primero guardar autor para obtener ID si no existe
        if (book.getAuthor() != null) {
            authorRepository.save(book.getAuthor());
        }
        return bookRepository.save(book);
    }

}
