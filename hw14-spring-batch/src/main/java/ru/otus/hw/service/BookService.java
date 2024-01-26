package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.mappers.BookConverter;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.output.BookOutput;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookConverter bookConverter;

    private final BookOutput bookOutput;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    public BookDocument doConversion(BookTable bookTable) {

        var convertedBook = bookConverter.convert(bookTable);
        var author = authorRepository.findByFullName(bookTable.getAuthor().getFullName());
        var genre = genreRepository.findByName(bookTable.getGenre().getName());
        convertedBook.setAuthor(author);
        convertedBook.setGenre(genre);

        return convertedBook;
    }

    public List<BookDocument> findAll() {
        return bookRepository.findAll()
                .stream()
                .toList();
    }
}
