package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@DisplayName("Проверка работы контроллера авторов")
@SpringBootTest
class AuthorControllerTest {

    private static List<AuthorDto> expectedAuthorsDto = new ArrayList<>();

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorController authorController;

    @Autowired
    private AuthorMapper authorMapper;

    @BeforeAll
    static void setExpectedAuthors() {
        expectedAuthorsDto = List.of(
                new AuthorDto("1L", "TestAuthor_1"),
                new AuthorDto("2L", "TestAuthor_2"),
                new AuthorDto("3L", "TestAuthor_3")
        );

    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnAllAuthors() {
        doReturn(Flux.fromStream(expectedAuthorsDto.stream()
                .map(authorMapper::toModel)))
                .when(authorRepository)
                .findAll();
        WebTestClient testClient = WebTestClient.bindToController(authorController).build();

        testClient.get()
                .uri("/api/v1/authors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AuthorDto.class)
                .isEqualTo(expectedAuthorsDto);

        verify(authorRepository, times(1)).findAll();
    }
}