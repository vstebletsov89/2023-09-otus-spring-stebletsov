package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.security.UserService;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера книг")
@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    static List<BookDto> expectedBooksDto = new ArrayList<>();

    @BeforeAll
    static void setExpectedBooks() {
        expectedBooksDto = List.of(
                new BookDto(1L, "TestBookTitle_1",
                        new AuthorDto(1L, "TestAuthor_1"),
                        new GenreDto(1L, "TestGenre_1")),
                new BookDto(2L, "TestBookTitle_2",
                        new AuthorDto(2L, "TestAuthor_2"),
                        new GenreDto(2L, "TestGenre_2")),
                new BookDto(3L, "TestBookTitle_3",
                        new AuthorDto(3L, "TestAuthor_3"),
                        new GenreDto(3L, "TestGenre_3"))
        );
    }

    @DisplayName("должен загружать список всех книг")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnAllBooks() throws Exception {
        doReturn(expectedBooksDto).when(bookService).findAll();

        mockMvc.perform(get("/api/v1/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBooksDto)));

        verify(bookService, times((1))).findAll();
    }

    @DisplayName("должен загружать книгу")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnBookById() throws Exception {
        var expectedBook = expectedBooksDto.get(0);
        doReturn(expectedBook).when(bookService).findById(expectedBook.getId());

        mockMvc.perform(get("/api/v1/books/{id}", expectedBook.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        verify(bookService, times((1))).findById(expectedBook.getId());
    }

    @DisplayName("должен добавить книгу")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldAddBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto(
                "NewBook",
                3L,
                3L);
        BookDto expectedBook = expectedBooksDto.get(0);
        doReturn(expectedBook).when(bookService).create(bookCreateDto);

        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookCreateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        verify(bookService, times((1))).create(bookCreateDto);
    }

    @DisplayName("должен обновить книгу")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldUpdateBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                1L,
                "UpdateBook",
                3L,
                3L);
        BookDto expectedBook = expectedBooksDto.get(0);
        doReturn(expectedBook).when(bookService).update(bookUpdateDto);

        mockMvc.perform(put("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookUpdateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));

        verify(bookService, times((1))).update(bookUpdateDto);
    }

    @DisplayName("должен удалить книгу")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldDeleteBook() throws Exception {
        var expectedBook = expectedBooksDto.get(0);
        doNothing().when(bookService).deleteById(anyLong());

        mockMvc.perform(delete("/api/v1/books/{id}", expectedBook.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(bookService, times((1))).deleteById(expectedBook.getId());
    }
}