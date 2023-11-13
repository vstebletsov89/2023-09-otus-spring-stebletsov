package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать список всех aвторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = authorRepositoryJpa.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);

        actualAuthors.forEach(System.out::println);
    }


    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author expectedAuthor) {
        var actualAuthor = authorRepositoryJpa.findById(expectedAuthor.getId());

        assertThat(actualAuthor).isPresent()
                        .get().isEqualTo(expectedAuthor);
    }

    @DisplayName("должен вернуть пустой результат для неверного id")
    @Test
    void shouldReturnEmptyResultForInvalidId() {
        var actualAuthor = authorRepositoryJpa.findById(99L);

        assertThat(actualAuthor).isNotPresent();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "TestAuthor_" + id))
                .toList();
    }
}