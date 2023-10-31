package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorRepositoryJdbc implements AuthorRepository {

    private static final String COLUMN_ID = "id";

    private static final String COLUMN_FULL_NAME = "full_name";

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcOperations.query("SELECT id, full_name FROM authors",
                new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.ofNullable(
                    namedParameterJdbcOperations.queryForObject(
                            "SELECT id, full_name FROM authors WHERE id = :id",
                            Map.of(COLUMN_ID, id),
                            new AuthorRowMapper()));
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(rs.getLong(COLUMN_ID),
                              rs.getString(COLUMN_FULL_NAME));
        }
    }
}
