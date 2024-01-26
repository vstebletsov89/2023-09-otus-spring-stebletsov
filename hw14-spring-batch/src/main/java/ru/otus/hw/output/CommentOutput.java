package ru.otus.hw.output;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.CommentDocument;

@Component
public class CommentOutput {

    public void printComment(CommentDocument commentDocument) {
        System.out.printf(
                "Id: %s, text: %s, bookId: {%s}%n",
                commentDocument.getId(),
                commentDocument.getText(),
                commentDocument.getBook().getId());
    }
}
