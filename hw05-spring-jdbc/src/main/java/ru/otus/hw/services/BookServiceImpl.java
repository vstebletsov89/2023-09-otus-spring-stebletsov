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
    public Book insert(String title, long authorId, long genreId) {
        return save(null, title, authorId, genreId);
    }

    @Override
    public Book update(Long id, String title, long authorId, long genreId) {
        return save(id, title, authorId, genreId);
    }

    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Book save(Long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId);
        if  (author == null) {
            throw new NotFoundException("Author with id %d not found".formatted(authorId));
        }
        var genre = genreRepository.findById(genreId);
        if  (genre == null) {
            throw new NotFoundException("Genre with id %d not found".formatted(genreId));
        }
        var book = new Book(id, title, author, genre);
        return bookRepository.save(book);
    }
}
