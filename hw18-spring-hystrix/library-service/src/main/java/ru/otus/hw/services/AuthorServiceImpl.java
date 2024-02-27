package ru.otus.hw.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @HystrixCommand(commandKey="getAuthors", fallbackMethod="buildFallbackAuthors")
    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(authorMapper::toDto)
                .toList();
    }

    @SuppressWarnings("unused")
    public List<AuthorDto> buildFallbackAuthors() {
        return Collections.emptyList();
    }

    @HystrixCommand(commandKey="getAuthor", fallbackMethod="buildFallbackAuthor")
    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(long id) {
        return authorMapper.toDto(
                authorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(id))));
    }

    @SuppressWarnings("unused")
    public AuthorDto buildFallbackAuthor(long id) {
        return new AuthorDto();
    }
}
