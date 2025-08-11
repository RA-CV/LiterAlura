package com.example.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @JsonAlias("languages")
    @Column(length = 10)
    private String language;// solo un idioma (el primero de la lista)

    @JsonAlias("download_count")
    private Integer downloadCount;

    // Constructor vacío
    public Book() {
    }

    // Constructor
    public Book(String title, Author author, String language, Integer downloadCount) {
        this.title = title;
        this.author = author;
        this.language = language;
        this.downloadCount = downloadCount;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    // toString()
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author=" + author.getName() +
                ", language='" + language + '\'' +
                ", downloadCount=" + downloadCount +
                '}';
    }

}
