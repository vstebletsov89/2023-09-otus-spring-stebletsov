package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.mappers.BookConverter;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookConverter bookConverter;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookDocument doConversion(BookTable bookTable) {

        var convertedBook = bookConverter.convert(bookTable);
        var author = authorRepository.findByFullName(bookTable.getAuthor().getFullName());
        var genre = genreRepository.findByName(bookTable.getGenre().getName());
        convertedBook.setAuthor(author);
        convertedBook.setGenre(genre);

        return convertedBook;
    }

}
