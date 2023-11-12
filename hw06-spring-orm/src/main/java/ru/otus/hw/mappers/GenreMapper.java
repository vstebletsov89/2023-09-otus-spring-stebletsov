package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;

@Mapper
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDto genreToGenreDto(Genre genre);
}
