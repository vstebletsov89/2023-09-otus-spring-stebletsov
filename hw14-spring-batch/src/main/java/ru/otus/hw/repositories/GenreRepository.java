package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.documents.GenreDocument;

public interface GenreRepository extends MongoRepository<GenreDocument, String> {

    GenreDocument findByName(String name);
}
