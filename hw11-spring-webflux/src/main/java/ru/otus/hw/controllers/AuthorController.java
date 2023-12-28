package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
public class AuthorController {
    private final AuthorRepository authorRepository;

    @GetMapping("/api/v1/authors")
    public Flux<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}