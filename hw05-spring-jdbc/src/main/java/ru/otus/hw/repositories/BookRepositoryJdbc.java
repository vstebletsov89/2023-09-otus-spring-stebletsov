package ru.otus.hw.repositories;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Book findById(long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                            """
                                    SELECT b.id, b.title,
                                      a.id AS author_id, a.full_name AS author_full_name, 
                                      g.id AS genre_id, g.name AS genre_name
                                    FROM books b
                                    JOIN genres  g ON b.genre_id  = g.id
                                    JOIN authors a ON b.author_id = a.id
                                    WHERE b.id = :id
                                    """,
                            Map.of("id", id),
                            new BookRowMapper());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Book> findAll() {
        return namedParameterJdbcTemplate.query(
                """
                        SELECT b.id, b.title,
                          a.id AS author_id, a.full_name AS author_full_name, 
                          g.id AS genre_id, g.name AS genre_name
                        FROM books b
                        JOIN genres  g ON b.genre_id  = g.id
                        JOIN authors a ON b.author_id = a.id
                        """,
                new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcTemplate.update(
                "DELETE FROM books WHERE id = :id",
                Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author_id, :genre_id)",
                new MapSqlParameterSource(
                        Map.of("title", book.getTitle(),
                                "author_id", book.getAuthor().getId(),
                                "genre_id", book.getGenre().getId())),
                keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        namedParameterJdbcTemplate.update(
                """
                        UPDATE books SET title = :title,
                        author_id = :author_id, genre_id = :genre_id WHERE id = :id
                        """,
                Map.of("id", book.getId(),
                        "title", book.getTitle(),
                        "author_id", book.getAuthor().getId(),
                        "genre_id", book.getGenre().getId()));
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
            Author author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
            return new Book(rs.getLong("id"), rs.getString("title"), author, genre);
        }
    }
}
