package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.security.UserService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.BookServiceImpl;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы глобального обработчика ошибок")
@SpringBootTest(classes = {
        BookController.class,
        BookServiceImpl.class,
        RestExceptionHandler.class,
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class
})
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
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

    @MockBean
    private UserService userService;

    @Autowired
    private BookController bookController;

    @Autowired
    private RestExceptionHandler restExceptionHandler;

    @DisplayName("должен обрабатывать исключение, когда книга не найдена")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldHandleNotFoundExceptionForBook() throws Exception {
        doReturn(Optional.empty()).when(bookRepository).findById(anyLong());

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Book with id 999 not found"));

        verify(bookRepository, times((1))).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключение, когда автор не найден")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldHandleNotFoundExceptionForAuthor() throws Exception {
        doReturn(Optional.empty()).when(authorRepository).findById(anyLong());
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new BookCreateDto("newTitle",
                                        999L,
                                        999L))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Author with id 999 not found"));

        verify(authorRepository, times((1))).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключение, когда жанр не найден")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldHandleNotFoundExceptionForGenre() throws Exception {
        doReturn(Optional.of(new Author())).when(authorRepository).findById(anyLong());
        doReturn(Optional.empty()).when(genreRepository).findById(anyLong());

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new BookCreateDto("newTitle",
                                        999L,
                                        999L))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("Genre with id 999 not found"));

        verify(authorRepository, times((1))).findById(anyLong());
    }

    @DisplayName("должен обрабатывать исключени внутренней ошибки сервера")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldHandleAnyRuntimeException() throws Exception {
        doReturn(Optional.of(new RuntimeException())).when(bookRepository).findById(anyLong());

        mockMvc.perform(get("/api/v1/books/{id}", 999))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content()
                        .string(containsString("class java.lang.RuntimeException")));

    }
}