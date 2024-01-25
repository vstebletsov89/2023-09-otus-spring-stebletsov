package ru.otus.hw.output;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.CommentDocument;

@Component
public class CommentOutput {

    public String commentToString(CommentDocument commentDocument) {
        return "Id: %s, text: %s, bookId: {%s}".formatted(
                commentDocument.getId(),
                commentDocument.getText(),
                commentDocument.getBook().getId());
    }
}
