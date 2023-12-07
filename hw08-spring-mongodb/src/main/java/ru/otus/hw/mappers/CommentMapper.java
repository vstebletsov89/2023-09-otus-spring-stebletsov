package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;


@Component
public class CommentMapper {

    public static CommentDto toDto(Comment comment) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setBookId(comment.getBook().getId());

        return commentDto;
    }

    public static Comment toModel(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                null);
    }
}
