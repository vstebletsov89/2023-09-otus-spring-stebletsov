package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @HystrixCommand(commandKey="getGenres", fallbackMethod="buildFallbackGenres")
    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @SuppressWarnings("unused")
    public List<GenreDto> buildFallbackGenres() {
        return Collections.emptyList();
    }

    @HystrixCommand(commandKey="getGenre", fallbackMethod="buildFallbackGenre")
    @Transactional(readOnly = true)
    @Override
    public GenreDto findById(long id) {
        return genreMapper.toDto(
                genreRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(id))));
    }

    @SuppressWarnings("unused")
    public GenreDto buildFallbackGenre(long id) {
        return new GenreDto();
    }
}
