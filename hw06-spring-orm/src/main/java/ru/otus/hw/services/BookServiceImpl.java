package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
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
    public BookDto findById(long id) {
        return BookMapper.bookToBookDto(
                bookRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id))));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(BookMapper::bookToBookDto)
                .toList();
    }

    @Transactional
    @Override
    public BookDto create(BookDto bookDto) {
        return save(bookDto.toModelObject());
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        var updatedBook =
                bookRepository.findById(bookDto.getId())
                        .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId()))
                );
        return save(bookDto.toModelObject());
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private BookDto save(Book book) {
        var author =
                authorRepository.findById(book.getAuthor().getId())
                        .orElseThrow(() -> new NotFoundException("Author with id %d not found"
                                .formatted(book.getAuthor().getId())
                        ));
        var genre =
                genreRepository.findById(book.getGenre().getId())
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found"
                                .formatted(book.getGenre().getId())
                        ));
        var newBook = new Book(book.getId(), book.getTitle(), author, genre);
        return BookMapper.bookToBookDto(bookRepository.save(newBook));
    }
}
