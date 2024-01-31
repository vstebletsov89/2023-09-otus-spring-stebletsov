package ru.otus.hw.mappers;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.documents.AuthorDocument;
import ru.otus.hw.models.tables.AuthorTable;

@Component
public class AuthorConverter implements Converter<AuthorTable, AuthorDocument> {

    @Override
    public AuthorDocument convert(AuthorTable source) {
        return new AuthorDocument(source.getFullName());
    }
}
