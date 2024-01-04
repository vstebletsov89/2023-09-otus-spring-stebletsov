package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest(classes = {
        BookController.class,
        RestExceptionHandler.class,
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class
})
class RestExceptionHandlerTest {
    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookController bookController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @DisplayName("должен обрабатывать исключение, когда книга не найдена")
    @Test
    void shouldHandleNotFoundException() {
        doReturn(Mono.empty()).when(bookRepository).findById(anyString());
        WebTestClient testClient = WebTestClient.bindToController(bookController)
                .controllerAdvice(restExceptionHandler)
                .build();

        var response = testClient.get()
                .uri("/api/v1/books/{id}", 123)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertEquals("Book with id 123 not found", response);
        verify(bookRepository, times((1))).findById(anyString());
    }

    @DisplayName("должен обрабатывать исключени внутренней ошибки сервера")
    @Test
    void shouldHandleAnyRuntimeException() {
        WebTestClient testClient = WebTestClient.bindToController(bookController)
                .controllerAdvice(restExceptionHandler)
                .build();

        var response = testClient.get()
                .uri("/invalid_uri")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is5xxServerError()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        System.out.println(response);
        assertThat(response).contains("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND");
    }
}