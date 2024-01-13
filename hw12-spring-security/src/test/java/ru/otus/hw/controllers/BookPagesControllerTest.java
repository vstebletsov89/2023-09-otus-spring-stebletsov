package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
                .andExpect(view().name("books/list"));
    }

    @DisplayName("должен загружать view редактирования книги")
    @Test
    void shouldReturnEditViewForBook() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"));
    }

    @DisplayName("должен загружать view для добавления книги")
    @Test
    void shouldReturnCreateViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/add"));
    }

    @DisplayName("должен загружать view для просмотра книги")
    @Test
    void shouldReturnInfoViewForBook() throws Exception {
        mockMvc.perform(get("/books/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/info"));
    }
}