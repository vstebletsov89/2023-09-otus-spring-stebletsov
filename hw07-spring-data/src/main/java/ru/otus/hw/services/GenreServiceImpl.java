package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(GenreMapper::genreToGenreDto)
                .toList();
    }

    @Override
    public GenreDto findById(long id) {
        return GenreMapper.genreToGenreDto(
                genreRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(id))));
    }
}
