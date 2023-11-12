package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BookRepositoryJpa implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(em.find(Book.class, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query =
                em.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }

    @Transactional
    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
             em.persist(book);
             return book;
        }
        return em.merge(book);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        em.remove(book);
    }
}
