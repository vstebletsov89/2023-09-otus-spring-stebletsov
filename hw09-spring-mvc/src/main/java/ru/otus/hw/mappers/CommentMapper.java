package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;


@RequiredArgsConstructor
@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setBookId(comment.getBook().getId());

        return commentDto;
    }

    public Comment toModel(CommentDto commentDto, Book book) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                book);
    }
}
