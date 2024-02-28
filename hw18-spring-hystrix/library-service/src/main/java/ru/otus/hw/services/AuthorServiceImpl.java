package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @CircuitBreaker(name = "getAuthors", fallbackMethod = "buildFallbackAuthors")
    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @SuppressWarnings("unused")
    public List<AuthorDto> buildFallbackAuthors(Throwable e) {
        return List.of(new AuthorDto(0, "fallbackAuthors"));
    }

    @CircuitBreaker(name = "getAuthors", fallbackMethod = "buildFallbackAuthor")
    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(long id) {
        return authorMapper.toDto(
                authorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(id))));
    }

    @SuppressWarnings("unused")
    public AuthorDto buildFallbackAuthor(Throwable e) {
        return new AuthorDto(0, "fallbackAuthor");
    }
}
