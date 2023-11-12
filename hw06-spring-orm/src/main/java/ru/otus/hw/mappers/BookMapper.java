package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    BookDto bookToBookDto(Book book);
}
