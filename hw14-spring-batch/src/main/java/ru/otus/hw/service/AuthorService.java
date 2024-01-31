package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
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
        var authorsMap = AuthorConfig.getJpaIdToMongoIdMap();
        authorsMap.put(authorTable.getId(), convertedAuthor.getId());

        return convertedAuthor;
    }
}

