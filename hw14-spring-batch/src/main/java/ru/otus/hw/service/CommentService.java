package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    public CommentDocument doConversion(CommentTable commentTable) {

        var convertedComment = commentConverter.convert(commentTable);
        var bookDocument =  bookRepository.findByTitle(commentTable.getBook().getTitle());
        convertedComment.setBook(bookDocument);

        return convertedComment;
    }

    public List<CommentDocument> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId)
                .stream()
                .toList();
    }
}