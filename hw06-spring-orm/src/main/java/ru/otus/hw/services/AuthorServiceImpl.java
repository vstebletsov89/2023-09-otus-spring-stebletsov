package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream()
                .map(AuthorMapper::authorToAuthorDto)
                .toList();
    }

    @Override
    public AuthorDto findById(long id) {
        return AuthorMapper.authorToAuthorDto(
                authorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(id))));
    }
}
