package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
import ru.otus.hw.mappers.BookMapper;
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

    private final BookMapper bookMapper;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getAllBooks() {
        return bookRepository.findAll().map(bookMapper::toDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<BookDto>> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books")
    public Mono<BookDto> addBook(@RequestBody @Valid BookCreateDto bookCreateDto) {
        log.info(bookCreateDto.toString());

        return authorRepository.findById(bookCreateDto.getAuthorId())
                .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found"
                                        .formatted(bookCreateDto.getAuthorId()))))
                .zipWith(genreRepository.findById(bookCreateDto.getGenreId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Genre with id %s not found"
                                        .formatted(bookCreateDto.getGenreId())))))
                .flatMap(relations -> {
                    log.info("author: " + relations.getT1().getFullName());
                    log.info("genre: " + relations.getT2().getName());

                    return bookRepository.save(
                            bookMapper.toModel(bookCreateDto,
                                    relations.getT1(),
                                    relations.getT2()))
                            .map(bookMapper::toDto);
                });
    }

    @PutMapping("/api/v1/books")
    public Mono<BookDto> updateBook(@RequestBody @Valid BookUpdateDto bookUpdateDto) {
        log.info(bookUpdateDto.toString());

        return authorRepository.findById(bookUpdateDto.getAuthorId())
                .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found"
                        .formatted(bookUpdateDto.getAuthorId()))))
                .zipWith(genreRepository.findById(bookUpdateDto.getGenreId())
                        .switchIfEmpty(Mono.error(new NotFoundException("Genre with id %s not found"
                                .formatted(bookUpdateDto.getGenreId())))))
                .flatMap(relations -> {
                    log.info("author: " + relations.getT1().getFullName());
                    log.info("genre: " + relations.getT2().getName());

                    return bookRepository.findById(bookUpdateDto.getId())
                            .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found"
                                    .formatted(bookUpdateDto.getId()))))
                            .flatMap(book -> bookRepository.save(
                                            bookMapper.toModel(bookUpdateDto,
                                                    relations.getT1(),
                                                    relations.getT2()))
                                    .map(bookMapper::toDto));
                });
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<Void> deleteBook (@PathVariable("id") String id) {
        return bookRepository.deleteById(id);
    }
}