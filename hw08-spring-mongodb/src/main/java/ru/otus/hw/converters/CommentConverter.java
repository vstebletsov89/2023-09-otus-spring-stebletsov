package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(CommentDto commentDto) {
        return "Id: %s, text: %s, bookId: {%s}".formatted(
                commentDto.getId(),
                commentDto.getText(),
                commentDto.getBookId());
    }
}
