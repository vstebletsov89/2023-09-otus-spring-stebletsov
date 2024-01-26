package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.documents.AuthorDocument;

public interface AuthorRepository extends MongoRepository<AuthorDocument, String> {

    AuthorDocument findByFullName(String name);
}
