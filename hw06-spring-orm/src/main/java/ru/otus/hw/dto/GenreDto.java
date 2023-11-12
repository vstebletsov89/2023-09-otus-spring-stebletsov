package ru.otus.hw.dto;

import lombok.Data;
import ru.otus.hw.models.Genre;

@Data
public class GenreDto {
    private long id;

    private String name;

    public Genre toModelObject() {
        return new Genre(id, name);
    }
}
