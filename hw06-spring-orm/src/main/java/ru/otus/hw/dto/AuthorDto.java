package ru.otus.hw.dto;

import lombok.Data;
import ru.otus.hw.models.Author;

@Data
public class AuthorDto {

    private long id;

    private String fullName;

    public Author toModelObject() {
        return new Author(id, fullName);
    }
}