package ru.otus.hw.repositories;

import ru.otus.hw.models.Genre;

import java.util.List;

public interface GenreRepository {
    List<Genre> findAll();

    Genre findById(long id);
}
