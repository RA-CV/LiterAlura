package com.example.literalura.repository;

import com.example.literalura.dto.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByLanguage(String language);

    List<Book> findByTitleContainingIgnoreCase(String title);

    long countByLanguage(String language);

}
