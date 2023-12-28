package ru.otus.hw.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private List<Mono<Author>> authors;

    private List<Mono<Genre>> genres;

    private List<Mono<Book>> books;

    @ChangeSet(order = "000", id = "dropDB", author = "vstebletsov", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "vstebletsov", runAlways = true)
    public void initAuthors(AuthorRepository repository) {
        var author1 = repository.save(new Author("Author_1"));
        var author2 = repository.save(new Author("Author_2"));
        var author3 = repository.save(new Author("Author_3"));
        authors = List.of(author1, author2, author3);
    }

    @ChangeSet(order = "002", id = "initGenres", author = "vstebletsov", runAlways = true)
    public void initGenres(GenreRepository repository) {
        var genre1 = repository.save(new Genre("Genre_1"));
        var genre2 = repository.save(new Genre("Genre_2"));
        var genre3 = repository.save(new Genre("Genre_3"));
        genres = List.of(genre1, genre2, genre3);
    }

    @ChangeSet(order = "003", id = "initBooks", author = "vstebletsov", runAlways = true)
    public void initBooks(BookRepository repository) {
        var book1 = repository.save(new Book("BookTitle_1", authors.get(0).block(), genres.get(0).block()));
        var book2 = repository.save(new Book("BookTitle_2", authors.get(1).block(), genres.get(1).block()));
        var book3 = repository.save(new Book("BookTitle_3", authors.get(2).block(), genres.get(2).block()));
        books = List.of(book1, book2, book3);
    }

    @ChangeSet(order = "004", id = "initComments", author = "vstebletsov", runAlways = true)
    public void initComments(CommentRepository repository) {
        repository.save(new Comment("CommentText_1", books.get(0).block()));
        repository.save(new Comment("CommentText_2", books.get(1).block()));
        repository.save(new Comment("CommentText_3", books.get(2).block()));
    }
}