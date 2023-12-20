package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDto> getAllGenres() {
        return genreService.findAll();
    }

    @GetMapping("/api/v1/genres/{id}")
    public GenreDto getGenreById(@PathVariable("id") long id) {
        return genreService.findById(id);
    }
}