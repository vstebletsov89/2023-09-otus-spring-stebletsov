package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findAllByBookId(long id);

    Comment save(Comment comment);

    void deleteById(long id);

    //TODO: implement repo
    //TODO: add dto and mapper
    //TODO: implement service
    //TODO: add tests for repo and service
    //TODO: add shell commands for comments

}
