package com.example.literalura.service;

import com.example.literalura.dto.Author;
import com.example.literalura.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAuthorsAliveInYear(int year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearIsNullOrDeathYearGreaterThan(year, year);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

}
