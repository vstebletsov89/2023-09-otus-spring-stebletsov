package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.CommentCreateDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.CommentUpdateDto;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.CommentMapperImpl;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Проверка работы контроллера комментариев")
@SpringBootTest(classes = {CommentController.class, CommentMapperImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest {

    private static List<CommentDto> expectedCommentsDto = new ArrayList<>();

    private static List<Comment> expectedComments = new ArrayList<>();

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentController commentController;

    @BeforeAll
    void setExpectedAuthors() {
        expectedComments = List.of(
                new Comment("1L", "TestCommentText_1",
                        new Book("1L", "BookTitle_1",
                                new Author("1L", "TestAuthor_1"),
                                new Genre("1L", "TestGenre_1"))),
                new Comment("2L", "TestCommentText_2",
                        new Book("2L", "BookTitle_2",
                                new Author("2L", "TestAuthor_2"),
                                new Genre("2L", "TestGenre_2"))),
                new Comment("3L", "TestCommentText_3",
                        new Book("3L", "BookTitle_3",
                                new Author("3L", "TestAuthor_3"),
                                new Genre("3L", "TestGenre_3"))));
        expectedCommentsDto =
                expectedComments.stream().map(commentMapper::toDto).toList();
    }

    @DisplayName("должен загружать список всех комментариев для книги")
    @Test
    void shouldReturnAllCommentsByBookId() {
        doReturn(Flux.fromStream(expectedComments.stream())).when(commentRepository).findAllByBookId(anyString());
        WebTestClient testClient = WebTestClient.bindToController(commentController).build();

        testClient.get()
                .uri("/api/v1/comments/book/{id}", expectedComments.get(0).getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(CommentDto.class)
                .isEqualTo(expectedCommentsDto);

        verify(commentRepository, times(1)).findAllByBookId(anyString());
    }

    @DisplayName("должен добавить комментарий")
    @Test
    void shouldAddComment() {
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                "NewComment",
                "3L");
        Comment expectedComment = expectedComments.get(0);
        doReturn(Mono.just(expectedComment.getBook())).when(bookRepository).findById(anyString());
        doReturn(Mono.just(expectedComment)).when(commentRepository).save(any(Comment.class));
        WebTestClient testClient = WebTestClient.bindToController(commentController).build();

        testClient.post()
                .uri("/api/v1/comments")
                .body(BodyInserters.fromValue(commentCreateDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentDto.class)
                .isEqualTo(expectedCommentsDto.get(0));

        verify(commentRepository, times((1))).save(any(Comment.class));
        verify(bookRepository, times((1))).findById(anyString());
    }

    @DisplayName("должен обновить комментарий")
    @Test
    void shouldUpdateComment() {
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(
                "1L",
                "UpdatedComment",
                "3L");
        Comment expectedComment = expectedComments.get(0);
        doReturn(Mono.just(expectedComment.getBook())).when(bookRepository).findById(anyString());
        doReturn(Mono.just(expectedComment)).when(commentRepository).save(any(Comment.class));
        WebTestClient testClient = WebTestClient.bindToController(commentController).build();

        testClient.put()
                .uri("/api/v1/comments")
                .body(BodyInserters.fromValue(commentUpdateDto))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentDto.class)
                .isEqualTo(expectedCommentsDto.get(0));

        verify(commentRepository, times((1))).save(any(Comment.class));
        verify(bookRepository, times((1))).findById(anyString());
    }

    @DisplayName("должен загружать комментарий")
    @Test
    void shouldReturnCommentById() {
        var expectedComment = expectedComments.get(0);
        doReturn(Mono.just(expectedComment)).when(commentRepository).findById(expectedComment.getId());
        WebTestClient testClient = WebTestClient.bindToController(commentController).build();

        testClient.get()
                .uri("/api/v1/comments/{id}", expectedComment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CommentDto.class)
                .isEqualTo(expectedCommentsDto.get(0));

        verify(commentRepository, times((1))).findById(expectedComment.getId());
    }
}