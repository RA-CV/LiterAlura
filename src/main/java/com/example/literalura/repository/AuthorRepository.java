package com.example.literalura.repository;

import com.example.literalura.dto.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Retorna autores vivos en un año dado cuyo birthYear <= year y deathYear es null o > year
    List<Author> findByBirthYearLessThanEqualAndDeathYearIsNullOrDeathYearGreaterThan(int year1, int year2);
    List<Author> findByDeathYearIsNull(); // autores vivos sin fecha de muerte
}
