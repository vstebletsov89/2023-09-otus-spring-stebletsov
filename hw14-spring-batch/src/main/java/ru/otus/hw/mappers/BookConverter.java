package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.AuthorConfig;
import ru.otus.hw.config.GenreConfig;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.tables.BookTable;


@RequiredArgsConstructor
@Component
public class BookConverter implements Converter<BookTable, BookDocument> {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    @Override
    public BookDocument convert(BookTable source) {

        var convertedBook = new BookDocument(
                source.getTitle(),
                authorConverter.convert(source.getAuthor()),
                genreConverter.convert(source.getGenre()));

        var authorId = AuthorConfig.getJpaIdToMongoIdMap().get(source.getAuthor().getId());
        var author = convertedBook.getAuthor();
        author.setId(authorId);

        var genreId = GenreConfig.getJpaIdToMongoIdMap().get(source.getGenre().getId());
        var genre = convertedBook.getGenre();
        genre.setId(genreId);

        convertedBook.setAuthor(author);
        convertedBook.setGenre(genre);
        convertedBook.setId(new ObjectId().toString());

        return convertedBook;
    }
}
