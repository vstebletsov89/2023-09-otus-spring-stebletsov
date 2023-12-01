package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import ru.otus.hw.models.Book;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Репозиторий для работы с книгами")
@DataMongoTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("получить все книги списком")
    @Test
    void shouldReturnAllBooks() {
        var actualBooks = bookRepository.findAll();

        assertEquals(3, actualBooks.size());
        assertEquals(Book.class, actualBooks.get(0).getClass());
    }
}