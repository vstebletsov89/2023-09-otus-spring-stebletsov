package ru.otus.hw.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "bookId", source = "book.id")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", source = "commentDto.id")
    Comment toModel(CommentDto commentDto, Book book);

    @Mapping(target = "book", source = "book")
    Comment toModel(CommentCreateDto commentCreateDto, Book book);
}
