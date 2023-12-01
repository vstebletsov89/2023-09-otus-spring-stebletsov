package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(String id);

    List<CommentDto> findAllByBookId(String id);

    CommentDto create(CommentDto book);

    CommentDto update(CommentDto book);

    void deleteById(String id);
}
