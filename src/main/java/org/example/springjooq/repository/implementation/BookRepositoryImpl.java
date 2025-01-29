package org.example.springjooq.repository.implementation;

import org.example.jooq.generated.tables.pojos.BookPojo;
import org.example.jooq.generated.tables.records.BookRecord;
import org.example.springjooq.repository.declaration.BookRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.example.jooq.generated.Tables.BOOK;

@Repository("bookRepository")
public class BookRepositoryImpl implements BookRepository {
    private final DSLContext ctx;

    public BookRepositoryImpl(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Optional<BookPojo> findById(Long id) {
        BookPojo book = ctx.selectFrom(BOOK)
                .where(BOOK.ID.eq(id))
                .fetchOneInto(BookPojo.class);

        return Optional.ofNullable(book);
    }

    @Override
    public Set<BookPojo> findAll() {
        var books = ctx.selectFrom(BOOK)
                .fetchInto(BookPojo.class);

        return new HashSet<>(books);
    }

    @Override
    public BookPojo create(BookPojo book) {
        BookRecord record = ctx.newRecord(BOOK, book);
        record.store();

        return record.into(BookPojo.class);
    }

    @Override
    public BookPojo update(long id, BookPojo book) {
        BookRecord record = ctx.fetchOne(BOOK, BOOK.ID.eq(id));
        if (record == null) {
            return null;
        }

        record.from(book);
        record.update();

        return record.into(BookPojo.class);
    }

    @Override
    public boolean delete(long id) {
        return ctx.deleteFrom(BOOK)
                .where(BOOK.ID.eq(id))
                .execute() > 0;
    }
}