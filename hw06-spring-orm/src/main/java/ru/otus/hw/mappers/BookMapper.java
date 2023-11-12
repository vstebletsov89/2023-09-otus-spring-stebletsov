package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Mapper(uses = {AuthorMapper.class, GenreMapper.class})
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    @Mapping(target = "authorDto", source = "book.author")
    @Mapping(target = "genreDto", source = "book.genre")
    BookDto bookToBookDto(Book book);
}
