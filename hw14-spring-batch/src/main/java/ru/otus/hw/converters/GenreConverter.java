package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.GenreDocument;

@Component
public class GenreConverter {
    public String genreToString(GenreDocument genreDocument) {
        return "Id: %d, Name: %s".formatted(genreDocument.getId(), genreDocument.getName());
    }
}
