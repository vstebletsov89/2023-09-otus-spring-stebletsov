package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(String id);

    List<BookDto> findAll();

    BookDto create(BookDto book);

    BookDto update(BookDto book);

    void deleteById(String id);
}
