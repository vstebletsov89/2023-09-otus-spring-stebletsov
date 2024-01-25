package ru.otus.hw.output;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.mappers.AuthorConverter;
import ru.otus.hw.models.documents.BookDocument;

@RequiredArgsConstructor
@Component
public class BookOutput {
    private final AuthorOutput authorOutput;

    private final GenreOutput genreOutput;

    public String bookToString(BookDocument book) {
        return "Id: %s, title: %s, author: {%s}, genres: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorOutput.authorToString(book.getAuthor()),
                genreOutput.genreToString(book.getGenre()));
    }
}
