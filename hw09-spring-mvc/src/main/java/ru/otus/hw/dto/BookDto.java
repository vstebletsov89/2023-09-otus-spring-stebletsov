package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private AuthorDto authorDto;

    @NotNull
    private GenreDto genreDto;

}
