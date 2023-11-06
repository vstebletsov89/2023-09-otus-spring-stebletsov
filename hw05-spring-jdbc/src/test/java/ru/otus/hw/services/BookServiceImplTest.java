package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса книг")
@JdbcTest
@Import({BookServiceImpl.class})
class BookServiceImplTest {

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    static List<Book> expectedBooks = new ArrayList<>();

    @BeforeAll
    static void setExpectedBooks() {
        expectedBooks = List.of(
                new Book(1L, "TestBook1",
                        new Author(1L, "TestAuthor1"),
                        new Genre(1L, "TestGenre1")),
                new Book(2L, "TestBook2",
                        new Author(2L, "TestAuthor2"),
                        new Genre(2L, "TestGenre2")),
                new Book(3L, "TestBook3",
                        new Author(3L, "TestAuthor3"),
                        new Genre(3L, "TestGenre3"))
        );
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        doReturn(expectedBooks).when(bookRepository).findAll();
        var actualBooks = bookService.findAll();

        assertEquals(3, actualBooks.size());
        assertEquals(expectedBooks, actualBooks);
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        long bookId = 2L;
        int bookPos = 1;
        doReturn(expectedBooks.get(bookPos)).when(bookRepository).findById(bookId);
        var actualBook = bookService.findById(bookId);

        assertEquals(expectedBooks.get(bookPos), actualBook);
    }


    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        Book expectedBook = expectedBooks.get(0);
        expectedBook.setId(null);
        doReturn(expectedBook).when(bookRepository).save(expectedBook);
        doReturn(expectedBook.getAuthor()).when(authorRepository).findById(expectedBook.getAuthor().getId());
        doReturn(expectedBook.getGenre()).when(genreRepository).findById(expectedBook.getGenre().getId());
        var actualBook = bookService.insert( new Book(
                null,
                expectedBook.getTitle(),
                expectedBook.getAuthor(),
                expectedBook.getGenre()));

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен обновить книгу")
    @Test
    void shouldUpdateBook() {
        Book newBook = expectedBooks.get(1);
        doReturn(newBook).when(bookRepository).save(newBook);
        doReturn(newBook).when(bookRepository).findById(2L);
        doReturn(newBook.getAuthor()).when(authorRepository).findById(newBook.getAuthor().getId());
        doReturn(newBook.getGenre()).when(genreRepository).findById(newBook.getGenre().getId());

        var actualBook = bookService.update( new Book(
                2L,
                newBook.getTitle(),
                newBook.getAuthor(),
                newBook.getGenre()));

        assertThat(actualBook).isEqualTo(newBook);
    }
}