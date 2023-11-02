package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса авторов")
@JdbcTest
@Import({AuthorServiceImpl.class})
class AuthorServiceImplTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    static List<Author> expectedAuthors = new ArrayList<>();

    @BeforeAll
    static void setExpectedAuthors() {
        expectedAuthors = List.of(
                new Author(1L, "TestAuthor1"),
                new Author(2L, "TestAuthor2"),
                new Author(3L, "TestAuthor3"));
    }

    @DisplayName("должен загружать список всех aвторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        doReturn(expectedAuthors).when(authorRepository).findAll();
        var actualAuthors = authorService.findAll();

        assertEquals(3, actualAuthors.size());
        assertEquals(expectedAuthors, actualAuthors);
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        long authorId = 2L;
        int authorPos = 1;
        doReturn(Optional.of(expectedAuthors.get(authorPos))).when(authorRepository).findById(authorId);
        var actualAuthor = authorService.findById(authorId);

        assertThat(actualAuthor).isPresent().get().isEqualTo(expectedAuthors.get(authorPos));
    }
}