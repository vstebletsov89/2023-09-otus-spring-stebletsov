package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("Репозиторий на основе Jpa для работы с коментариями")
@DataJpaTest
@Import({CommentRepositoryJpa.class})
class CommentRepositoryJpaTest {

    @Autowired
    private CommentRepositoryJpa commentRepositoryJpa;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<Comment> dbComments;

    @BeforeEach
    void setUp() {
        dbComments = getDbComments();
    }

    @DisplayName("должен загружать коментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = dbComments.get(0);
        var actualComment = commentRepositoryJpa.findById(expectedComment.getId());

        assertThat(actualComment).isPresent()
                .get().isEqualTo(expectedComment);
    }

    @DisplayName("должен вернуть пустой результат для неверного id")
    @Test
    void shouldReturnEmptyResultForInvalidId() {
        var actualComment = commentRepositoryJpa.findById(99L);

        assertThat(actualComment).isNotPresent();
    }

    @DisplayName("должен загружать список всех коментариев для книги")
    @Test
    void shouldReturnCorrectCommentsByBookId() {
        var actualComments = commentRepositoryJpa.findAllByBookId(1L);
        var expectedComments = List.of(dbComments.get(0));

        assertEquals(1, actualComments.size());

        assertEquals(expectedComments, actualComments);
    }

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(null, "newComment",  null);
        var returnedComment = commentRepositoryJpa.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .isEqualTo(expectedComment);

        assertThat(commentRepositoryJpa.findById(returnedComment.getId())).
                isPresent().get().isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный коментарий")
    @Test
    void shouldSaveUpdatedComment() {
        var expectedComment = new Comment(1L, "updatedComment",  null);

        assertNotEquals(expectedComment, commentRepositoryJpa.findById(expectedComment.getId()));

        var returnedComment = commentRepositoryJpa.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .isEqualTo(expectedComment);

        assertThat(commentRepositoryJpa.findById(returnedComment.getId())).
                isPresent().get().isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять коментарий по id ")
    @Test
    void shouldDeleteComment() {
        assertThat(commentRepositoryJpa.findById(3L)).isPresent();

        commentRepositoryJpa.deleteById(3L);

        assertThat(commentRepositoryJpa.findById(3L)).isEmpty();
    }

    private List<Comment> getDbComments() {
        TypedQuery<Comment> query = testEntityManager.getEntityManager()
                .createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }
}