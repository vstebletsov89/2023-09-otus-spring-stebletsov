package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;

@RequiredArgsConstructor
@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        return "Id: %d, title: %s, author: {%s}, genre: [%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthorDto()),
                genreConverter.genreToString(book.getGenreDto()));
    }

    public String bookCreateToString(BookCreateDto book) {
        return "Title: %s, author: {%s}, genre: [%s]".formatted(
                book.getTitle(),
                authorConverter.authorToString(book.getAuthorDto()),
                genreConverter.genreToString(book.getGenreDto()));
    }
}
