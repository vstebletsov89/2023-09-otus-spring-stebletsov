package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {
    private final BookConverter bookConverter;

    public String commentToString(CommentDto commentDto) {
        return "Id: %d, text: %s, book: {%s}".formatted(
                commentDto.getId(),
                commentDto.getText(),
                bookConverter.bookToString(commentDto.getBookDto()));
    }
}
