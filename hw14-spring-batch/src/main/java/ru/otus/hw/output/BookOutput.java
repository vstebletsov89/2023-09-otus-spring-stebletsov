package ru.otus.hw.output;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.BookDocument;

@RequiredArgsConstructor
@Component
public class BookOutput {
    private final AuthorOutput authorOutput;

    private final GenreOutput genreOutput;

    public void printBook(BookDocument book) {
        System.out.printf(
                "Id: %s, title: %s, author: {%s}, genre: [%s]%n",
                book.getId(),
                book.getTitle(),
                authorOutput.authorToString(book.getAuthor()),
                genreOutput.genreToString(book.getGenre()));
    }
}
