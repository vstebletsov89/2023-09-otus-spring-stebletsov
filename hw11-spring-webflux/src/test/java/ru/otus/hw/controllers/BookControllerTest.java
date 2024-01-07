package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.AuthorMapperImpl;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.BookMapperImpl;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы контроллера книг")
@SpringBootTest(classes = {
        BookController.class,
        BookMapperImpl.class,
        AuthorMapperImpl.class,
        GenreMapperImpl.class
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerTest {

    private static List<BookDto> expectedBooksDto = new ArrayList<>();

    private static List<Book> expectedBooks = new ArrayList<>();

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private BookController bookController;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private GenreMapper genreMapper;

    @BeforeAll
    void setExpectedBooks() {
        expectedBooks = List.of(
                new Book("1L", "TestBookTitle_1",
                        new Author("1L", "TestAuthor_1"),
                        new Genre("1L", "TestGenre_1")),
                new Book("2L", "TestBookTitle_2",
                        new Author("2L", "TestAuthor_2"),
                        new Genre("2L", "TestGenre_2")),
                new Book("3L", "TestBookTitle_3",
                        new Author("3L", "TestAuthor_3"),
                        new Genre("3L", "TestGenre_3"))
        );
        expectedBooksDto = expectedBooks.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnAllBooks() {
        doReturn(Flux.fromStream(expectedBooks.stream())).when(bookRepository).findAll();
        WebTestClient testClient = WebTestClient.bindToController(bookController).build();

        testClient.get()
                .uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .isEqualTo(expectedBooksDto);

        verify(bookRepository, times((1))).findAll();
    }

    @DisplayName("должен загружать книгу")
    @Test
    void shouldReturnBookById() {
        var expectedBook = expectedBooks.get(0);
        doReturn(Mono.just(expectedBook)).when(bookRepository).findById(expectedBook.getId());
        WebTestClient testClient = WebTestClient.bindToController(bookController).build();

        testClient.get()
                .uri("/api/v1/books/{id}", expectedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedBooksDto.get(0));

        verify(bookRepository, times((1))).findById(expectedBook.getId());
    }

    @DisplayName("должен добавить книгу")
    @Test
    void shouldAddBook() {
        BookCreateDto bookCreateDto = new BookCreateDto(
                "NewBook",
                "3L",
                "3L");
        Book expectedBook = expectedBooks.get(0);
        doReturn(Mono.just(expectedBook)).when(bookRepository).save(any(Book.class));
        doReturn(Mono.just(expectedBook.getAuthor())).when(authorRepository).findById(anyString());
        doReturn(Mono.just(expectedBook.getGenre())).when(genreRepository).findById(anyString());
        WebTestClient testClient = WebTestClient.bindToController(bookController).build();

        testClient.post()
                .uri("/api/v1/books")
                .body(BodyInserters.fromValue(bookCreateDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedBooksDto.get(0));

        verify(bookRepository, times((1))).save(any(Book.class));
        verify(authorRepository, times((1))).findById(anyString());
        verify(genreRepository, times((1))).findById(anyString());
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto("1L", "UpdateBook", "3L", "3L");
        Book expectedBook = expectedBooks.get(0);
        doReturn(Mono.just(expectedBook)).when(bookRepository).findById(anyString());
        doReturn(Mono.just(expectedBook)).when(bookRepository).save(any(Book.class));
        doReturn(Mono.just(expectedBook.getAuthor())).when(authorRepository).findById(anyString());
        doReturn(Mono.just(expectedBook.getGenre())).when(genreRepository).findById(anyString());
        WebTestClient testClient = WebTestClient.bindToController(bookController).build();

        testClient.put()
                .uri("/api/v1/books")
                .body(BodyInserters.fromValue(bookUpdateDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BookDto.class)
                .isEqualTo(expectedBooksDto.get(0));

        verify(bookRepository, times((1))).save(any(Book.class));
        verify(bookRepository, times((1))).findById(anyString());
        verify(authorRepository, times((1))).findById(anyString());
        verify(genreRepository, times((1))).findById(anyString());
    }

    @DisplayName("должен удалить книгу")
    @Test
    void shouldDeleteBook() {
        var expectedBook = expectedBooks.get(0);
        doReturn(Mono.empty()).when(bookRepository).deleteById(anyString());
        WebTestClient testClient = WebTestClient.bindToController(bookController).build();

        testClient.delete()
                .uri("/api/v1/books/{id}", expectedBook.getId())
                .exchange()
                .expectStatus()
                .isOk();

        verify(bookRepository, times((1))).deleteById(expectedBook.getId());
    }
}