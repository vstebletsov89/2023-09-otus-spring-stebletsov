package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @GetMapping("/api/v1/comments/{id}")
    public Flux<CommentDto> getCommentsByBookId(@PathVariable("id") String id) {
        return commentRepository.findAllByBookId(id).map(commentMapper::toDto);
    }

    @PostMapping("/api/v1/comments")
    public Mono<CommentDto> addComment(@RequestBody @Valid CommentCreateDto commentCreateDto) {
        log.info(commentCreateDto.toString());

        return bookRepository.findById(commentCreateDto.getBookId())
                .switchIfEmpty(Mono.error(new NotFoundException("Book with id %s not found"
                        .formatted(commentCreateDto.getBookId()))))
                .flatMap(book -> {
                    log.info("book: " + book.getTitle());

                    return commentRepository.save(
                                    commentMapper.toModel(commentCreateDto,
                                            book))
                            .map(commentMapper::toDto);
                });
    }
}
