package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    AuthorDto authorToAuthorDto(Author author);
}
