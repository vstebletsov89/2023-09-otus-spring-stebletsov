package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
        convertedGenre.setId(new ObjectId().toString());
        var genresMap = GenreConfig.getJpaIdToMongoObjectMap();
        genresMap.put(genreTable.getId(), convertedGenre);
        return convertedGenre;
    }
}