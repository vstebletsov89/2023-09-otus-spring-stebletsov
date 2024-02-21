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

@DisplayName("Проверка работы контроллера страниц для комментариев с авторизацией")
@WebMvcTest(CommentPagesController.class)
@Import(SecurityConfiguration.class)
class CommentPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @DisplayName("должен загружать страницу редактирования комментария для пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnViewForEditComment() throws Exception {
        mockMvc.perform(get("/comments/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("comments/edit"));
    }

    @DisplayName("должен запрещать страницу редактирования комментария для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void forbiddenViewForEditComment() throws Exception {
        mockMvc.perform(get("/comments/edit"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен возвращать страницу логина для редактирования комментария")
    @Test
    void shouldReturnLoginPageForEditComment() throws Exception {
        mockMvc.perform(get("/comments/edit"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("должен загружать страницу добавления комментария для пользователя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_USER"}
    )
    @Test
    void shouldReturnViewForAddComment() throws Exception {
        mockMvc.perform(get("/comments/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("comments/add"));
    }

    @DisplayName("должен запрещать страницу добавления комментария для гостя")
    @WithMockUser(
            username = "user",
            authorities = {"ROLE_GUEST"}
    )
    @Test
    void forbiddenViewForAddComment() throws Exception {
        mockMvc.perform(get("/comments/add"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("должен возвращать страницу логина для добавления комментария")
    @Test
    void shouldReturnLoginPageForAddComment() throws Exception {
        mockMvc.perform(get("/comments/add"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}