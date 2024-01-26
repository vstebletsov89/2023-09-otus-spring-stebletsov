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

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    @Override
    public BookDocument convert(BookTable source) {
        return new BookDocument(source.getTitle(),
                authorConverter.convert(source.getAuthor()),
                genreConverter.convert(source.getGenre()));
    }
}