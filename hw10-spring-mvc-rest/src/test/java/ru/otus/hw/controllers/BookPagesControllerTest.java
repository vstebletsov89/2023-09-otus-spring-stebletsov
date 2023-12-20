package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Проверка работы контроллера книг")
@WebMvcTest(BookPagesController.class)
class BookPagesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    static List<BookDto> expectedBooksDto = new ArrayList<>();

    @BeforeAll
    static void setExpectedBooks() {
        expectedBooksDto = List.of(
                new BookDto(1L, "TestBook1",
                        new AuthorDto(1L, "TestAuthor1"),
                        new GenreDto(1L, "TestGenre1")),
                new BookDto(2L, "TestBook2",
                        new AuthorDto(2L, "TestAuthor2"),
                        new GenreDto(2L, "TestGenre2")),
                new BookDto(3L, "TestBook3",
                        new AuthorDto(3L, "TestAuthor3"),
                        new GenreDto(3L, "TestGenre3"))
        );
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnViewWithAllBooks() throws Exception {
        doReturn(expectedBooksDto).when(bookService).findAll();

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDtoList", expectedBooksDto))
                .andExpect(content().string(containsString(expectedBooksDto.get(0).getTitle())))
                .andExpect(content().string(containsString(expectedBooksDto.get(0).getAuthorDto().getFullName())))
                .andExpect(content().string(containsString(expectedBooksDto.get(0).getGenreDto().getName())))
                .andExpect(view().name("list"));
    }

    @DisplayName("должен загружать view редактирования книги")
    @Test
    void shouldReturnEditViewForBook() throws Exception {
        BookDto expectedBook = expectedBooksDto.get(1);
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                expectedBook.getId(),
                expectedBook.getTitle(),
                expectedBook.getAuthorDto(),
                expectedBook.getGenreDto()
        );
        List<AuthorDto> authorDtoList = List.of(expectedBook.getAuthorDto());
        List<GenreDto> genreDtoList = List.of(expectedBook.getGenreDto());
        doReturn(expectedBook).when(bookService).findById(2L);
        doReturn(authorDtoList).when(authorService).findAll();
        doReturn(genreDtoList).when(genreService).findAll();

        mockMvc.perform(get("/book/edit").param("id", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookUpdateDto", bookUpdateDto))
                .andExpect(model().attribute("authorDtoList", authorDtoList))
                .andExpect(model().attribute("genreDtoList", genreDtoList))
                .andExpect(content().string(containsString(expectedBook.getTitle())))
                .andExpect(content().string(containsString(expectedBook.getAuthorDto().getFullName())))
                .andExpect(content().string(containsString(expectedBook.getGenreDto().getName())))
                .andExpect(view().name("edit"));

        verify(bookService, times(1)).findById(2);
        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
    }

    @DisplayName("должен обновить книгу и сделать редирект")
    @Test
    void shouldUpdateBookAndRedirect() throws Exception {
        BookDto expectedBookDto = expectedBooksDto.get(0);
        BookUpdateDto bookUpdateDto = new BookUpdateDto(
                expectedBookDto.getId(),
                expectedBookDto.getTitle(),
                expectedBookDto.getAuthorDto(),
                expectedBookDto.getGenreDto()
        );

        mockMvc.perform(post("/book/edit").flashAttr("bookUpdateDto", bookUpdateDto))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).update(bookUpdateDto);
    }

    @DisplayName("должен загружать view для добавления книги")
    @Test
    void shouldReturnCreateViewForBook() throws Exception {
        BookDto expectedBook = expectedBooksDto.get(1);
        List<AuthorDto> authorDtoList = List.of(expectedBook.getAuthorDto());
        List<GenreDto> genreDtoList = List.of(expectedBook.getGenreDto());
        doReturn(authorDtoList).when(authorService).findAll();
        doReturn(genreDtoList).when(genreService).findAll();

        mockMvc.perform(get("/book/create"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("bookCreateDto"))
                .andExpect(model().attribute("authorDtoList", authorDtoList))
                .andExpect(model().attribute("genreDtoList", genreDtoList))
                .andExpect(content().string(containsString(expectedBook.getAuthorDto().getFullName())))
                .andExpect(content().string(containsString(expectedBook.getGenreDto().getName())))
                .andExpect(view().name("create"));

        verify(authorService, times(1)).findAll();
        verify(genreService, times(1)).findAll();
    }

    @DisplayName("должен сохранить книгу и сделать редирект")
    @Test
    void shouldSaveBookAndRedirect() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("NewBook",
                new AuthorDto(3L, "Author"),
                new GenreDto(3L, "Genre"));

        mockMvc.perform(post("/book/create").flashAttr("bookCreateDto", bookCreateDto))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).create(bookCreateDto);
    }

    @DisplayName("должен удалить книгу и сделать редирект")
    @Test
    void shouldDeleteBookAndRedirect() throws Exception {
        doNothing().when(bookService).deleteById(anyLong());

        mockMvc.perform(post("/book/delete").param("id", "3"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).deleteById(3L);
    }
}