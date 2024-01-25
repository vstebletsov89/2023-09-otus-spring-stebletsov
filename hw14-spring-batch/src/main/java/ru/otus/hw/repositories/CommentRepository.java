package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.documents.CommentDocument;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentDocument, String> {

      List<CommentDocument> findAllByBookId(String bookId);
}
