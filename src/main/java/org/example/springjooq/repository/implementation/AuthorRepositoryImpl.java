package org.example.springjooq.repository.implementation;




import org.example.jooq.generated.tables.pojos.AuthorPojo;
import org.example.jooq.generated.tables.records.AuthorRecord;
import org.example.springjooq.repository.declaration.AuthorRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.example.jooq.generated.Tables.AUTHOR;

@Repository("authorRepository")
public class AuthorRepositoryImpl implements AuthorRepository {
    private final DSLContext ctx;

    public AuthorRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Optional<AuthorPojo> findById(long id) {
        AuthorPojo author = ctx.selectFrom(AUTHOR)
                .where(AUTHOR.ID.eq(id))
                .fetchOneInto(AuthorPojo.class);

        return Optional.ofNullable(author);
    }

    @Override
    public Set<AuthorPojo> findAll() {
        var list = ctx.selectFrom(AUTHOR)
                .fetchInto(AuthorPojo.class);

        return new HashSet<>(list);
    }

    @Override
    public AuthorPojo create(AuthorPojo author) {
        AuthorRecord record = ctx.newRecord(AUTHOR, author);
        record.store();
        return record.into(AuthorPojo.class);
    }

    @Override
    public AuthorPojo update(long id, AuthorPojo author) {
        AuthorRecord record = ctx.fetchOne(AUTHOR, AUTHOR.ID.eq(id));
        if (record == null) {
            return null;
        }
        record.from(author);
        record.update();

        return record.into(AuthorPojo.class);
    }

    @Override
    public boolean delete(long id) {
        return ctx.deleteFrom(AUTHOR)
                .where(AUTHOR.ID.eq(id))
                .execute() > 0;
    }
}
