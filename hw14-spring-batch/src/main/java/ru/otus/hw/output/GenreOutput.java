package ru.otus.hw.output;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.GenreDocument;

@Component
public class GenreOutput {
    public String genreToString(GenreDocument genreDocument) {
        return "Id: %s, Name: %s".formatted(genreDocument.getId(), genreDocument.getName());
    }
}
