package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.AuthorDocument;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.documents.GenreDocument;
import ru.otus.hw.models.tables.AuthorTable;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.models.tables.GenreTable;


@RequiredArgsConstructor
@Component
public class BookConverter implements Converter<BookTable, BookDocument> {

    private final Converter<AuthorTable, AuthorDocument> authorDocumentConverter;

    private final Converter<GenreTable, GenreDocument> genreDocumentConverter;

    @Override
    public BookDocument convert(BookTable source) {
        return new BookDocument(source.getTitle(),
                    authorDocumentConverter.convert(source.getAuthor()),
                    genreDocumentConverter.convert(source.getGenre()));
    }
}
