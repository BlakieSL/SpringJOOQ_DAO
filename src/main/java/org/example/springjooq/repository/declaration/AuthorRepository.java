package org.example.springjooq.repository.declaration;


import org.example.jooq.generated.tables.pojos.AuthorPojo;

import java.util.Optional;
import java.util.Set;

public interface AuthorRepository {
    Optional<AuthorPojo> findById(long id);
    Set<AuthorPojo> findAll();
    AuthorPojo create(AuthorPojo author);
    AuthorPojo update(long id, AuthorPojo author);
    boolean delete(long id);
}
