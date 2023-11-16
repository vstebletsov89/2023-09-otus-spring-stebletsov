package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;


@Component
public class CommentMapper {

    public static CommentDto commentToCommentDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setBookId(comment.getBook().getId());

        return commentDto;
    }
}
