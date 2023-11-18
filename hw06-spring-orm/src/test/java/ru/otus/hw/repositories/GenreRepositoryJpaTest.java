package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
@Import({GenreRepositoryJpa.class})
class GenreRepositoryJpaTest {

    @Autowired
    private GenreRepositoryJpa genreRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = genreRepositoryJpa.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);

        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        var expectedGenre = dbGenres.get(0);
        var actualGenre = genreRepositoryJpa.findById(expectedGenre.getId());

        assertThat(actualGenre).isPresent()
                        .get().isEqualTo(expectedGenre);
    }

    @DisplayName("должен вернуть пустой результат для неверного id")
    @Test
    void shouldReturnEmptyResultForInvalidId() {
        var actualGenre = genreRepositoryJpa.findById(99L);

        assertThat(actualGenre).isNotPresent();
    }

    private List<Genre> getDbGenres() {
        TypedQuery<Genre> query = testEntityManager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }
}