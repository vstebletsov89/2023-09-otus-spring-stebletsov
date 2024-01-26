package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.mappers.CommentConverter;
import ru.otus.hw.models.documents.CommentDocument;
import ru.otus.hw.models.tables.CommentTable;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentConverter commentConverter;

    private final BookRepository bookRepository;

    public CommentDocument doConversion(CommentTable commentTable) {

        var convertedComment = commentConverter.convert(commentTable);
        var bookDocument =  bookRepository.findByTitle(commentTable.getBook().getTitle());
        convertedComment.setBook(bookDocument);

        return convertedComment;
    }

}