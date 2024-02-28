package ru.otus.hw.feign;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;

import java.util.List;

@FeignClient(name = "library-service")
public interface LibraryServiceProxy {

    @GetMapping("/api/v1/books")
    List<BookDto> getAllBooks();

    @GetMapping("/api/v1/books/{id}")
    BookDto getBookById(@PathVariable("id") long id);

    @PostMapping("/api/v1/books")
    BookDto addBook(@RequestBody @Valid BookCreateDto bookCreateDto);

    @PutMapping("/api/v1/books")
    BookDto updateBook(@RequestBody @Valid BookUpdateDto bookUpdateDto);

    @DeleteMapping("/api/v1/books/{id}")
    void deleteBook(@PathVariable("id") long id);
}