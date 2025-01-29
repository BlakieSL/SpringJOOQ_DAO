package org.example.springjooq.repository.declaration;

import org.example.jooq.generated.tables.pojos.BookPojo;

import java.util.Optional;
import java.util.Set;

public interface BookRepository {
    Optional<BookPojo> findById(Long id);
    Set<BookPojo> findAll();
    BookPojo create(BookPojo book);
    BookPojo update(long id, BookPojo book);
    boolean delete(long id);
}
