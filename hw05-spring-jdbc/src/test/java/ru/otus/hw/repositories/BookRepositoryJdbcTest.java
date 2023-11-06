package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий на основе Jdbc для работы с книгами")
@JdbcTest
@Import({BookRepositoryJdbc.class, GenreRepositoryJdbc.class})
class BookRepositoryJdbcTest {

    @Autowired
    private BookRepositoryJdbc repositoryJdbc;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
    }

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getDbBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = repositoryJdbc.findById(expectedBook.getId());

        assertEquals(expectedBook, actualBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = repositoryJdbc.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);

        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        var returnedBook = repositoryJdbc.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertEquals(returnedBook, repositoryJdbc.findById(returnedBook.getId()));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertNotEquals(expectedBook, repositoryJdbc.findById(expectedBook.getId()));

        var returnedBook = repositoryJdbc.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertEquals(returnedBook, repositoryJdbc.findById(returnedBook.getId()));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {

        assertNotNull(repositoryJdbc.findById(1L));

        repositoryJdbc.deleteById(1L);

        assertNull(repositoryJdbc.findById(1L));
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "TestAuthor_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Genre(id, "TestGenre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return LongStream.range(1, 4).boxed()
                .map(id -> new Book(id, "TestBookTitle_" + id,
                        dbAuthors.get((id.intValue() - 1)),
                        dbGenres.get(id.intValue() - 1)))
                .toList();
    }

    private static List<Book> getDbBooks() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        return getDbBooks(dbAuthors, dbGenres);
    }
}