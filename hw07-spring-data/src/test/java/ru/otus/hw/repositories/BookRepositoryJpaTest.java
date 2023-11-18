package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@Import({BookRepositoryJpa.class})
class BookRepositoryJpaTest {

    @Autowired
    private BookRepositoryJpa bookRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks();
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldReturnCorrectBookById() {
        var expectedBook = dbBooks.get(0);
        var actualBook = bookRepositoryJpa.findById(expectedBook.getId());

        assertThat(actualBook).isPresent()
                        .get().isEqualTo(expectedBook);
    }

    @DisplayName("должен вернуть пустой результат для неверного id")
    @Test
    void shouldReturnEmptyResultForInvalidId() {
        var actualBook = bookRepositoryJpa.findById(99L);

        assertThat(actualBook).isNotPresent();
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepositoryJpa.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);

        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(null, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        var returnedBook = bookRepositoryJpa.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepositoryJpa.findById(returnedBook.getId())).isPresent()
                        .get().isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertNotEquals(expectedBook, bookRepositoryJpa.findById(expectedBook.getId()));

        var returnedBook = bookRepositoryJpa.save(expectedBook);

        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepositoryJpa.findById(returnedBook.getId())).isPresent()
                        .get().isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {

        assertThat(bookRepositoryJpa.findById(1L)).isPresent();

        bookRepositoryJpa.deleteById(1L);

        assertThat(bookRepositoryJpa.findById(1L)).isEmpty();
    }

    private List<Author> getDbAuthors() {
        TypedQuery<Author> query = testEntityManager.getEntityManager()
                .createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    private List<Genre> getDbGenres() {
        TypedQuery<Genre> query = testEntityManager.getEntityManager()
                .createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    private List<Book> getDbBooks() {
        TypedQuery<Book> query = testEntityManager.getEntityManager()
                .createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }
}