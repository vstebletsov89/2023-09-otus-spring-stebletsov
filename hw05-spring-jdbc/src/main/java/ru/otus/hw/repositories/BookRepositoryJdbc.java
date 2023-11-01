package ru.otus.hw.repositories;

import org.springframework.dao.DataAccessException;
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
import java.util.Optional;

@Repository
public class BookRepositoryJdbc implements BookRepository {

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_AUTHOR_ID = "author_id";

    private static final String COLUMN_AUTHOR_FULL_NAME = "author_full_name";

    private static final String COLUMN_GENRE_ID = "genre_id";

    private static final String COLUMN_GENRE_NAME = "genre_name";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(
                            """
                                    SELECT b.id, b.title,
                                      a.id AS author_id, a.full_name AS author_full_name, 
                                      g.id AS genre_id, g.name AS genre_name
                                    FROM books b
                                    JOIN genres  g ON b.genre_id  = g.id
                                    JOIN authors a ON b.author_id = a.id
                                    WHERE id = :id
                                    """,
                            Map.of(COLUMN_ID, id),
                            new BookRowMapper()));
        } catch (DataAccessException ex) {
            return Optional.empty();
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
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcTemplate.update(
                "DELETE FROM books WHERE id = :id",
                Map.of(COLUMN_ID, id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(
                "INSERT INTO books (title, author_id, genre_id) VALUES (:title, :author_id, :genre_id)",
                new MapSqlParameterSource(
                        Map.of(COLUMN_TITLE, book.getTitle(),
                                COLUMN_AUTHOR_ID, book.getAuthor().getId(),
                                COLUMN_GENRE_ID, book.getGenre().getId())),
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
                Map.of(COLUMN_ID, book.getId(),
                        COLUMN_TITLE, book.getTitle(),
                        COLUMN_AUTHOR_ID, book.getAuthor().getId(),
                        COLUMN_GENRE_ID, book.getGenre().getId()));
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Genre genre = new Genre(rs.getLong(COLUMN_GENRE_ID), rs.getString(COLUMN_GENRE_NAME));
            Author author = new Author(rs.getLong(COLUMN_AUTHOR_ID), rs.getString(COLUMN_AUTHOR_FULL_NAME));
            return new Book(rs.getLong(COLUMN_ID), rs.getString(COLUMN_TITLE), author, genre);
        }
    }
}
