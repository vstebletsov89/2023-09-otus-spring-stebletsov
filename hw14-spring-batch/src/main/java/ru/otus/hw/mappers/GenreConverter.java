package ru.otus.hw.mappers;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.GenreDocument;
import ru.otus.hw.models.tables.GenreTable;

@Component
public class GenreConverter implements Converter<GenreTable, GenreDocument> {

    @Override
    public GenreDocument convert(GenreTable source) {
        return new GenreDocument(source.getName());
    }
}
