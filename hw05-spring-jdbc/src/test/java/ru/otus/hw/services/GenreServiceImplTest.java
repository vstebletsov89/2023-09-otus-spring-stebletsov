package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса жанров")
@JdbcTest
@Import({GenreServiceImpl.class})
class GenreServiceImplTest {

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    static List<Genre> expectedGenres = new ArrayList<>();

    @BeforeAll
    static void setExpectedGenres() {
        expectedGenres = List.of(
                new Genre(1L, "TestGenre1"),
                new Genre(2L, "TestGenre2"),
                new Genre(3L, "TestGenre3")
        );
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        doReturn(expectedGenres).when(genreRepository).findAll();
        var actualGenres = genreService.findAll();

        assertEquals(3, actualGenres.size());
        assertEquals(expectedGenres, actualGenres);
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        long genreId = 1L;
        int genrePos = 0;
        doReturn(Optional.of(expectedGenres.get(genrePos))).when(genreRepository).findById(genreId);
        var actualGenre = genreService.findById(genreId);

        assertThat(actualGenre).isPresent().get().isEqualTo(expectedGenres.get(genrePos));
    }
}