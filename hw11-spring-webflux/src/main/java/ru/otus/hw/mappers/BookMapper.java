package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;


@Mapper(componentModel = "spring")
public interface BookMapper {

     BookDto toDto(Book book);

     Book toModel(BookUpdateDto bookUpdateDto, Author author, Genre genre);

     Book toModel(BookCreateDto bookCreateDto, Author author, Genre genre);

     Book toModel(BookDto bookDto, Author author, Genre genre);
}
