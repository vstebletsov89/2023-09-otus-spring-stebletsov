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
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка работы контроллера жанров")
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    static List<GenreDto> expectedGenresDto = new ArrayList<>();

    @BeforeAll
    static void setExpectedGenres() {
        expectedGenresDto = List.of(
                new GenreDto(1L, "TestGenre_1"),
                new GenreDto(2L, "TestGenre_2"),
                new GenreDto(3L, "TestGenre_3")
        );
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnAllGenres() throws Exception {
        doReturn(expectedGenresDto).when(genreService).findAll();

        mockMvc.perform(get("/api/v1/genres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenresDto)));
    }

    @DisplayName("должен загружать жанр")
    @Test
    void shouldReturnGenreById() throws Exception {
        var expectedGenre = expectedGenresDto.get(0);
        doReturn(expectedGenre).when(genreService).findById(expectedGenre.getId());

        mockMvc.perform(get("/api/v1/genres/{id}", expectedGenre.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedGenre)));
    }
}