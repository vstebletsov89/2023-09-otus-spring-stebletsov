package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.AuthorConfig;
import ru.otus.hw.mappers.AuthorConverter;
import ru.otus.hw.models.documents.AuthorDocument;
import ru.otus.hw.models.tables.AuthorTable;

@RequiredArgsConstructor
@Service
public class AuthorService {

    private final AuthorConverter authorConverter;

    public AuthorDocument doConversion(AuthorTable authorTable) {

        var convertedAuthor = authorConverter.convert(authorTable);
        convertedAuthor.setId(new ObjectId().toString());
        var authorsMap = AuthorConfig.getJpaIdToMongoObjectMap();
        authorsMap.put(authorTable.getId(), convertedAuthor);
        return convertedAuthor;
    }
}

