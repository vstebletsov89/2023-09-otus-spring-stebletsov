//package ru.otus.hw.services;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import ru.otus.hw.dto.AuthorDto;
//import ru.otus.hw.exceptions.NotFoundException;
//import ru.otus.hw.mappers.AuthorMapper;
//import ru.otus.hw.models.tables.AuthorTables;
//import ru.otus.hw.repositories.AuthorRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.doReturn;
//
//@DisplayName("Проверка работы сервиса авторов")
//@SpringBootTest(classes = {AuthorServiceImpl.class})
//class AuthorServiceImplTest {
//  TODO: change to job tests for config
//    @MockBean
//    private AuthorRepository authorRepository;
//
//    @Autowired
//    private AuthorService authorService;
//
//    static List<AuthorTables> expectedAuthors = new ArrayList<>();
//    static List<AuthorDto> expectedAuthorsDto = new ArrayList<>();
//
//    @BeforeAll
//    static void setExpectedAuthors() {
//        expectedAuthors = List.of(
//                new AuthorTables(1L, "TestAuthor1"),
//                new AuthorTables(2L, "TestAuthor2"),
//                new AuthorTables(3L, "TestAuthor3"));
//        expectedAuthorsDto =
//                expectedAuthors.stream()
//                        .map(AuthorMapper::toDto)
//                        .toList();
//    }
//
//    @DisplayName("должен загружать список всех aвторов")
//    @Test
//    void shouldReturnCorrectAuthorsList() {
//        doReturn(expectedAuthors).when(authorRepository).findAll();
//        var actualAuthors = authorService.findAll();
//
//        assertEquals(3, actualAuthors.size());
//        assertEquals(expectedAuthorsDto, actualAuthors);
//    }
//
//    @DisplayName("должен загружать автора по id")
//    @Test
//    void shouldReturnCorrectAuthorById() {
//        long authorId = 2L;
//        int authorPos = 1;
//        doReturn(Optional.of(expectedAuthors.get(authorPos))).when(authorRepository).findById(authorId);
//        var actualAuthor = authorService.findById(authorId);
//
//        assertThat(actualAuthor)
//                .usingRecursiveComparison()
//                .isEqualTo(expectedAuthorsDto.get(authorPos));
//    }
//
//    @DisplayName("должен выбрасывать исключение для неверного id")
//    @Test
//    void shouldReturnExceptionForInvalidId() {
//        doReturn(Optional.empty()).when(authorRepository).findById(99L);
//        var exception = assertThrows(NotFoundException.class,
//                () -> authorService.findById(99L));
//
//        assertEquals("Author with id 99 not found", exception.getMessage());
//    }
//}