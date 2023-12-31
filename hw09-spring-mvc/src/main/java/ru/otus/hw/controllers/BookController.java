package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> bookDtoList = bookService.findAll();
        model.addAttribute("bookDtoList", bookDtoList);
        return "list";
    }

    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.findById(id);
        log.info("get/book/edit");
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                bookDto.getId(),
                bookDto.getTitle(),
                bookDto.getAuthorDto(),
                bookDto.getGenreDto()
        );
        log.info(bookUpdateDto.toString());

        model.addAttribute("bookUpdateDto", bookUpdateDto);
        loadAuthorsAndGenresToModel(model);
        return "edit";
    }

    @PostMapping("/book/edit")
    public String updateBook(@Valid BookUpdateDto bookUpdateDto,
                           BindingResult bindingResult, Model model) {
        log.info("post/book/edit");
        log.info(bookUpdateDto.toString());

        if (bindingResult.hasErrors()) {
            loadAuthorsAndGenresToModel(model);
            return "edit";
        }
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }

    @GetMapping("/book/create")
    public String addPage(Model model) {
        log.info("get/book/create");
        BookCreateDto bookCreateDto = new BookCreateDto();
        model.addAttribute("bookCreateDto", bookCreateDto);
        loadAuthorsAndGenresToModel(model);
        return "create";
    }

    @PostMapping("/book/create")
    public String addBook(@Valid BookCreateDto bookCreateDto,
                          BindingResult bindingResult, Model model) {
        log.info("post/book/create");
        log.info(bookCreateDto.toString());
        if (bindingResult.hasErrors()) {
            loadAuthorsAndGenresToModel(model);
            return "create";
        }
        bookService.create(bookCreateDto);
        return "redirect:/";
    }

    @PostMapping("/book/delete")
    public String deleteBook (@RequestParam("id") long id) {
        log.info("post/book/delete");
        bookService.deleteById(id);
        return "redirect:/";
    }

    private void loadAuthorsAndGenresToModel(Model model) {
        List<AuthorDto> authorDtoList = authorService.findAll();
        List<GenreDto> genreDtoList = genreService.findAll();
        model.addAttribute("authorDtoList", authorDtoList);
        model.addAttribute("genreDtoList", genreDtoList);
    }
}