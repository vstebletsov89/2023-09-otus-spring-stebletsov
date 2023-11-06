package ru.otus.hw.services;

import ru.otus.hw.models.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> findAll();

    Book insert(String title, long authorId, long genreId);

    Book update(Long id, String title, long authorId, long genreId);

    void deleteById(long id);
}
