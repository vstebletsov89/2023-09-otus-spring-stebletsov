package ru.otus.hw.services;

import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto create(BookCreateDto book);

    BookDto update(BookDto book);

    void deleteById(long id);
}
