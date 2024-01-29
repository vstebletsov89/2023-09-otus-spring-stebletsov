package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.AuthorConfig;
import ru.otus.hw.config.BookConfig;
import ru.otus.hw.config.GenreConfig;
import ru.otus.hw.mappers.BookConverter;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookConverter bookConverter;

    private final BookRepository bookRepository;

    public BookDocument doConversion(BookTable bookTable) {

        var convertedBook = bookConverter.convert(bookTable);
        var author = AuthorConfig.getJpaIdToMongoObjectMap().get(bookTable.getAuthor().getId());
        var genre = GenreConfig.getJpaIdToMongoObjectMap().get(bookTable.getGenre().getId());
        convertedBook.setAuthor(author);
        convertedBook.setGenre(genre);
        convertedBook.setId(new ObjectId().toString());
        var booksMap = BookConfig.getJpaIdToMongoObjectMap();
        booksMap.put(bookTable.getId(), convertedBook);
        return convertedBook;
    }

    public List<BookDocument> findAll() {
        return bookRepository.findAll()
                .stream()
                .toList();
    }
}
