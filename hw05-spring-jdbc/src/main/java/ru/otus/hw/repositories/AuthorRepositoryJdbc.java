package ru.otus.hw.repositories;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuthorRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Author> findAll() {
        return namedParameterJdbcTemplate.query("SELECT id, full_name FROM authors",
                new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(
                            "SELECT id, full_name FROM authors WHERE id = :id",
                            Map.of("id", id),
                            new AuthorRowMapper()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(rs.getLong("id"),
                              rs.getString("full_name"));
        }
    }
}
