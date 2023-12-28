package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;


@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    //TODO: fix it

    @GetMapping("/api/v1/books")
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<Book> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id);
    }

    @PostMapping("/api/v1/books")
    public BookDto addBook(@RequestBody @Valid BookCreateDto bookCreateDto) {
        log.info(bookCreateDto.toString());
        var author =
                authorRepository.findById(bookCreateDto.getAuthorId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found"
                                .formatted(bookCreateDto.getAuthorId())
                        ));
        var genre =
                genreRepository.findById(bookCreateDto.getGenreId())
                        .orElseThrow(() -> new NotFoundException("Genre with id %d not found"
                                .formatted(bookCreateDto.getGenreId())
                        ));
        var newBook = bookMapper.toModel(bookCreateDto, author, genre);
        return bookMapper.toDto(bookRepository.save(newBook));
    }



    @PutMapping("/api/v1/books")
    public BookDto updateBook(@RequestBody @Valid BookUpdateDto bookUpdateDto) {
        log.info(bookUpdateDto.toString());
        return bookService.update(bookUpdateDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook (@PathVariable("id") long id) {
        bookService.deleteById(id);
    }
}