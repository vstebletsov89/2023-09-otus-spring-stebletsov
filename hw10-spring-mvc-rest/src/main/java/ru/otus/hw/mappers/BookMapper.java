package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


@RequiredArgsConstructor
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public BookDto toDto(Book book) {

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(authorMapper.toDto(book.getAuthor()));
        bookDto.setGenreDto(genreMapper.toDto(book.getGenre()));

        return bookDto;
    }

    public Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre) {
        return new Book(bookUpdateDto.getId(),
                bookUpdateDto.getTitle(),
                author,
                genre);
    }

    public Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre) {
        return new Book(null,
                bookCreateDto.getTitle(),
                author,
                genre);
    }

    public Book toModel(BookDto bookDto, Author author, Genre genre) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                author,
                genre);
    }
}
