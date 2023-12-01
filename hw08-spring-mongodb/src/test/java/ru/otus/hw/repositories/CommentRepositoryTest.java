package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий для работы с коментариями")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("получить все коментарии для книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("title").is("TestBookTitle_1"));
        var book = mongoTemplate.findOne(query1, Book.class);
        var actualComments = commentRepository.findAllByBookId(book.getId());

        assertEquals(1, actualComments.size());
        assertEquals("TestCommentText_1", actualComments.get(0).getText());
        assertEquals(Comment.class, actualComments.get(0).getClass());
    }
}