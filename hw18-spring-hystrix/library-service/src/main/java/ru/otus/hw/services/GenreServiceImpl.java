package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @CircuitBreaker(name = "getGenres", fallbackMethod = "buildFallbackGenres")
    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @SuppressWarnings("unused")
    public List<GenreDto> buildFallbackGenres(Throwable e) {
        return List.of(new GenreDto(0, "fallbackGenre"));
    }

    @CircuitBreaker(name = "getGenre", fallbackMethod = "buildFallbackGenre")
    @Transactional(readOnly = true)
    @Override
    public GenreDto findById(long id) {
        return genreMapper.toDto(
                genreRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(id))));
    }

    @SuppressWarnings("unused")
    public GenreDto buildFallbackGenre(Throwable e) {
        return new GenreDto();
    }
}
