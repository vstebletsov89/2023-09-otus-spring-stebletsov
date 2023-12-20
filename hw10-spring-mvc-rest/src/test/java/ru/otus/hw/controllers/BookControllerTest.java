package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера книг")
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

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
    @Test
    void shouldReturnAllBooks() throws Exception {
        doReturn(expectedBooksDto).when(bookService).findAll();

        mockMvc.perform(get("/api/v1/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBooksDto)));
    }

    @DisplayName("должен загружать книгу")
    @Test
    void shouldReturnBookById() throws Exception {
        var expectedBook = expectedBooksDto.get(0);
        doReturn(expectedBook).when(bookService).findById(expectedBook.getId());

        mockMvc.perform(get("/api/v1/books/{id}", expectedBook.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedBook)));
    }

    @Test
    void addBook() {
        //TODO:implement it
    }

    @Test
    void updateBook() {
        //TODO:implement it
    }

    @Test
    void deleteBook() {
        //TODO:implement it
    }

    //TODO: add tests for pages
}