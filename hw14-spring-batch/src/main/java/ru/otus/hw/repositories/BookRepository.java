package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.documents.BookDocument;

public interface BookRepository extends MongoRepository<BookDocument, String> {

}
