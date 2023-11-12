package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Book;

@Data
@AllArgsConstructor
public class BookDto {
    private Long id;

    private String title;

    private AuthorDto authorDto;

    private GenreDto genreDto;

    public Book toModelObject() {

        return new Book(id, title, authorDto.toModelObject(), genreDto.toModelObject());
    }
}
