package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Проверка работы контроллера страниц для книги")
@WebMvcTest(BookPagesController.class)
class BookPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("должен загружать страницу со списком книг")
    @Test
    void shouldReturnViewWithAllBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("list"));
    }

    @DisplayName("должен загружать view редактирования книги")
    @Test
    void shouldReturnEditViewForBook() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("edit"));
    }

    @DisplayName("должен загружать view для добавления книги")
    @Test
    void shouldReturnCreateViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }

    @DisplayName("должен загружать view для просмотра книги")
    @Test
    void shouldReturnInfoViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("add"));
    }
}