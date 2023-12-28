package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentDto toDto(Comment comment);

    Comment toModel(CommentDto commentDto, Book book);

    Comment toModel(CommentCreateDto commentCreateDto, Book book);
}
