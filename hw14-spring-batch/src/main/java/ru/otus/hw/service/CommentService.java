package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.BookConfig;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentConverter;
import ru.otus.hw.models.documents.CommentDocument;
import ru.otus.hw.models.tables.CommentTable;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentConverter commentConverter;

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentDocument doConversion(CommentTable commentTable) {

        var convertedComment = commentConverter.convert(commentTable);
        var bookId = BookConfig.getJpaIdToMongoIdMap().get(commentTable.getBook().getId());
        var bookDocument = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book with id " + bookId + " not found"));
        convertedComment.setBook(bookDocument);

        return convertedComment;
    }

    public List<CommentDocument> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream()
                .toList();
    }
}