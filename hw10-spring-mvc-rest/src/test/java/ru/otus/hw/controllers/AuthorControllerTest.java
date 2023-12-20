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
import ru.otus.hw.services.AuthorService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Проверка работы контроллера авторов")
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    static List<AuthorDto> expectedAuthorsDto = new ArrayList<>();

    @BeforeAll
    static void setExpectedAuthors() {
        expectedAuthorsDto = List.of(
                new AuthorDto(1L, "TestAuthor_1"),
                new AuthorDto(2L, "TestAuthor_2"),
                new AuthorDto(3L, "TestAuthor_3")
        );
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnAllAuthors() throws Exception {
        doReturn(expectedAuthorsDto).when(authorService).findAll();

        mockMvc.perform(get("/api/v1/authors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthorsDto)));
    }

    @DisplayName("должен загружать автора")
    @Test
    void shouldReturnAuthorById() throws Exception {
        var expectedAuthor = expectedAuthorsDto.get(0);
        doReturn(expectedAuthor).when(authorService).findById(expectedAuthor.getId());

        mockMvc.perform(get("/api/v1/authors/{id}", expectedAuthor.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAuthor)));
    }
}