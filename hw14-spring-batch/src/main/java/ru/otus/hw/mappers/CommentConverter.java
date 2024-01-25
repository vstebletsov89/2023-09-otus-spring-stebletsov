package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.BookDocument;
import ru.otus.hw.models.documents.CommentDocument;
import ru.otus.hw.models.tables.BookTable;
import ru.otus.hw.models.tables.CommentTable;

@RequiredArgsConstructor
@Component
public class CommentConverter implements Converter<CommentTable, CommentDocument> {

    private final Converter<BookTable, BookDocument> bookDocumentConverter;

    @Override
    public CommentDocument convert(CommentTable source) {
        return new CommentDocument(source.getText(),
                bookDocumentConverter.convert(source.getBook()));
    }
}
