package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Book findById(long id) {
        var book = bookRepository.findById(id);
        if  (book == null) {
            throw new NotFoundException("Book with id %d not found".formatted(id));
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(Book book) {
        return save(book);
    }

    @Override
    public Book update(Book book) {
        var updatedBook = bookRepository.findById(book.getId());
        if  (updatedBook == null) {
            throw new NotFoundException("Book with id %d not found".formatted(book.getId()));
        }

        return save(book);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Book book) {
        var author = authorRepository.findById(book.getAuthor().getId());
        if  (author == null) {
            throw new NotFoundException("Author with id %d not found".formatted(book.getAuthor().getId()));
        }
        var genre = genreRepository.findById(book.getGenre().getId());
        if  (genre == null) {
            throw new NotFoundException("Genre with id %d not found".formatted(book.getGenre().getId()));
        }
        var newBook = new Book(book.getId(), book.getTitle(), author, genre);
        return bookRepository.save(newBook);
    }
}
