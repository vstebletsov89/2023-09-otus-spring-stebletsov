package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.GenreConfig;
import ru.otus.hw.mappers.GenreConverter;
import ru.otus.hw.models.documents.GenreDocument;
import ru.otus.hw.models.tables.GenreTable;

@RequiredArgsConstructor
@Service
public class GenreService {

    private final GenreConverter genreConverter;

    public GenreDocument doConversion(GenreTable genreTable) {

        var convertedGenre = genreConverter.convert(genreTable);
        var genresMap = GenreConfig.getJpaIdToMongoIdMap();
        genresMap.put(genreTable.getId(), convertedGenre.getId());

        return convertedGenre;
    }
}