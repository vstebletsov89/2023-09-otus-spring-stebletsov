package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.BookConfig;
import ru.otus.hw.models.documents.CommentDocument;
import ru.otus.hw.models.tables.CommentTable;

@RequiredArgsConstructor
@Component
public class CommentConverter implements Converter<CommentTable, CommentDocument> {

    private final BookConverter bookConverter;

    @Override
    public CommentDocument convert(CommentTable source) {

        var convertedComment = new CommentDocument(source.getText(),
                bookConverter.convert(source.getBook()));

        var bookId = BookConfig.getJpaIdToMongoIdMap().get(source.getBook().getId());
        var book = convertedComment.getBook();
        book.setId(bookId);
        convertedComment.setBook(book);

        return convertedComment;
    }
}
