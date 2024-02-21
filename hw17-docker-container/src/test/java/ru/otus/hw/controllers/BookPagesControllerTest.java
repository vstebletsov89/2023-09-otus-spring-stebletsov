package ru.otus.hw.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.security.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Проверка работы контроллера страниц для книги с авторизацией")
@WebMvcTest(BookPagesController.class)
@Import(SecurityConfiguration.class)
class BookPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("должен загружать страницу со списком книг для пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnViewWithAllBooksForUser() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"));
    }

    @DisplayName("должен загружать страницу со списком книг для админа")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    void shouldReturnViewWithAllBooksForAdmin() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"));
    }

    @DisplayName("должен загружать страницу со списком книг для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void shouldReturnViewWithAllBooksForGuest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"));
    }

    @DisplayName("должен возвращать страницу логина для списка книг")
    @Test
    void shouldReturnLoginPageForAllBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("должен загружать view редактирования книги пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnEditViewForBookForUser() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"));
    }

    @DisplayName("должен запрещать view редактирования книги для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void forbiddenEditViewForBookForGuest() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен возвращать страницу логина для редактирования книги")
    @Test
    void shouldReturnLoginPageForViewForBook() throws Exception {
        mockMvc.perform(get("/books/edit"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("должен загружать view для добавления книги")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnCreateViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/add"));
    }

    @DisplayName("должен запрещать view для добавления книги для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void forbiddenCreateViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен возвращать страницу логина для добавления книги")
    @Test
    void shouldReturnLoginPageForCreateViewForBook() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("должен загружать view для просмотра книги")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnInfoViewForBook() throws Exception {
        mockMvc.perform(get("/books/info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("books/info"));
    }

    @DisplayName("должен запрещать view для просмотра книги для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void forbiddenInfoViewForBook() throws Exception {
        mockMvc.perform(get("/books/info"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен возвращать страницу логина для просмотра книги")
    @Test
    void shouldReturnLoginPageForInfoViewForBook() throws Exception {
        mockMvc.perform(get("/books/info"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}