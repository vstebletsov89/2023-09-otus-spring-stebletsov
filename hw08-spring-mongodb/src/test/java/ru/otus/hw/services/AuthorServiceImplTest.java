package ru.otus.hw.services;

import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса авторов")
@SpringBootTest(classes = {AuthorServiceImpl.class})
class AuthorServiceImplTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    static List<Author> expectedAuthors = new ArrayList<>();
    static List<AuthorDto> expectedAuthorsDto = new ArrayList<>();

    @BeforeAll
    static void setExpectedAuthors() {
        expectedAuthors = List.of(
                new Author("1", "TestAuthor1"),
                new Author("2", "TestAuthor2"),
                new Author("3", "TestAuthor3"));
        expectedAuthorsDto =
                expectedAuthors.stream()
                        .map(AuthorMapper::toDto)
                        .toList();
    }

    @DisplayName("должен загружать список всех aвторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        doReturn(expectedAuthors).when(authorRepository).findAll();
        var actualAuthors = authorService.findAll();

        assertEquals(3, actualAuthors.size());
        assertEquals(expectedAuthorsDto, actualAuthors);
    }

    @DisplayName("должен загружать автора по id")
    @Test
    void shouldReturnCorrectAuthorById() {
        String authorId = "2";
        int authorPos = 1;
        doReturn(Optional.of(expectedAuthors.get(authorPos))).when(authorRepository).findById(authorId);
        var actualAuthor = authorService.findById(authorId);

        assertThat(actualAuthor)
                .usingRecursiveComparison()
                .isEqualTo(expectedAuthorsDto.get(authorPos));
    }

    @DisplayName("должен выбрасывать исключение для неверного id")
    @Test
    void shouldReturnExceptionForInvalidId() {
        doReturn(Optional.empty()).when(authorRepository).findById("99");
        var exception = assertThrows(NotFoundException.class,
                () -> authorService.findById("99"));

        assertEquals("Author with id 99 not found", exception.getMessage());
    }
}