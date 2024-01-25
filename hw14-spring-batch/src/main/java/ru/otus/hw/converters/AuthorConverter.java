package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.AuthorDocument;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDocument author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
