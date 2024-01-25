package ru.otus.hw.output;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.AuthorDocument;

@Component
public class AuthorOutput {

    public String authorToString(AuthorDocument author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
