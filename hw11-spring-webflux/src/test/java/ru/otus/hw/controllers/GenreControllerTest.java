package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы контроллера жанров")
@SpringBootTest
class GenreControllerTest {

    private static List<GenreDto> expectedGenresDto = new ArrayList<>();

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreController genreController;

    @Autowired
    private GenreMapper genreMapper;

    @BeforeAll
    static void setExpectedGenres() {
        expectedGenresDto = List.of(
                new GenreDto("1L", "TestGenre_1"),
                new GenreDto("2L", "TestGenre_2"),
                new GenreDto("3L", "TestGenre_3")
        );
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnAllGenres() {
        doReturn(Flux.fromStream(expectedGenresDto.stream().map(genreMapper::toModel))).when(genreRepository).findAll();
        WebTestClient testClient = WebTestClient.bindToController(genreController).build();

        testClient.get()
                .uri("/api/v1/genres")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(GenreDto.class)
                .isEqualTo(expectedGenresDto);

        verify(genreRepository, times(1)).findAll();
    }
}