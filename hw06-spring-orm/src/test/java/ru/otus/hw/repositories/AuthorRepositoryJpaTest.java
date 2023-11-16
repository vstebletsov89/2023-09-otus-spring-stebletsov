package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({AuthorRepositoryJpa.class})
class AuthorRepositoryJpaTest {

    @Autowired
    private AuthorRepositoryJpa authorRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

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
    @Test
    void shouldReturnCorrectAuthorById() {
        var expectedAuthor = dbAuthors.get(0);
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

    private List<Author> getDbAuthors() {
        TypedQuery<Author> query = testEntityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }
}