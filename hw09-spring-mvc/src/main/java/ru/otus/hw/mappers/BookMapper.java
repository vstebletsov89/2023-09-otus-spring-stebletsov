package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;


@Component
public class BookMapper {

    public static BookDto toDto(Book book) {

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthorDto(AuthorMapper.toDto(book.getAuthor()));
        bookDto.setGenreDto(GenreMapper.toDto(book.getGenre()));

        return bookDto;
    }

    public static Book toModel(BookDto bookDto) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                AuthorMapper.toModel(bookDto.getAuthorDto()),
                GenreMapper.toModel(bookDto.getGenreDto()));
    }

    public static Book toModel(BookCreateDto bookCreateDto) {
        return new Book(null,
                bookCreateDto.getTitle(),
                AuthorMapper.toModel(bookCreateDto.getAuthorDto()),
                GenreMapper.toModel(bookCreateDto.getGenreDto()));
    }
}
