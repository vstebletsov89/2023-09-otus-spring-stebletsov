package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.CommentDocument;

@Component
public class CommentConverter {

    public String commentToString(CommentDocument commentDocument) {
        return "Id: %d, text: %s, bookId: {%d}".formatted(
                commentDocument.getId(),
                commentDocument.getText(),
                commentDocument.getBook().getId());
    }
}
