package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@WebMvcTest(controllers = {
        RestExceptionHandler.class,
        BookController.class
},
        includeFilters = {})
class RestExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

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
    void shouldHandleNotFoundExceptionForBook() throws Exception {
        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());
        var response = "ffff";

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        assertEquals("Book with id 999 not found", response);
        verify(bookRepository, times((1))).findById(anyLong());
    }
//
//    @DisplayName("должен обрабатывать исключение, когда автор не найден")
//    @Test
//    void shouldHandleNotFoundExceptionForAuthor() {
//        doReturn(Optional.empty()).when(authorRepository).findById(anyString());
//        doReturn(Optional.empty()).when(genreRepository).findById(anyString());
//        WebTestClient testClient = WebTestClient.bindToController(bookController)
//                .controllerAdvice(restExceptionHandler)
//                .build();
//
//        var response = testClient.post()
//                .uri("/api/v1/books")
//                .body(BodyInserters.fromValue(new BookCreateDto("newTitle", "999", "999")))
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isNotFound()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertEquals("Author with id 999 not found", response);
//        verify(authorRepository, times((1))).findById(anyString());
//    }
//
//    @DisplayName("должен обрабатывать исключение, когда жанр не найден")
//    @Test
//    void shouldHandleNotFoundExceptionForGenre() {
//        doReturn(Mono.just(new Author())).when(authorRepository).findById(anyString());
//        doReturn(Mono.empty()).when(genreRepository).findById(anyString());
//        WebTestClient testClient = WebTestClient.bindToController(bookController)
//                .controllerAdvice(restExceptionHandler)
//                .build();
//
//        var response = testClient.post()
//                .uri("/api/v1/books")
//                .body(BodyInserters.fromValue(new BookCreateDto("newTitle", "999", "999")))
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isNotFound()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertEquals("Genre with id 999 not found", response);
//        verify(genreRepository, times((1))).findById(anyString());
//    }
//
//    @DisplayName("должен обрабатывать исключени внутренней ошибки сервера")
//    @Test
//    void shouldHandleAnyRuntimeException() {
//        WebTestClient testClient = WebTestClient.bindToController(bookController)
//                .controllerAdvice(restExceptionHandler)
//                .build();
//
//        var response = testClient.get()
//                .uri("/invalid_uri")
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .is5xxServerError()
//                .expectBody(String.class)
//                .returnResult()
//                .getResponseBody();
//
//        assertThat(response).contains("org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND");
//    }
}