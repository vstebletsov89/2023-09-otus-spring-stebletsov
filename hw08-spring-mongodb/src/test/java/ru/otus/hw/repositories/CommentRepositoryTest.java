package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.hw.models.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий для работы с коментариями")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("получить все коментарии для книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        String id = "3";
        var actualComments = commentRepository.findAllByBookId(id);

        assertEquals(1, actualComments.size());
        assertEquals(Comment.class, actualComments.get(0).getClass());
    }
}