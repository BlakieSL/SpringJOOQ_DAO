package org.example.springjooq;

import org.example.jooq.generated.tables.pojos.BookPojo;
import org.example.springjooq.repository.declaration.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the jOOQ-based BookRepository.
 * Adjust IDs, fields, or date checks to match your insert-data.sql as needed.
 */
@SpringBootTest
public class BookRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepositoryTest.class);

    @Autowired
    private BookRepository bookRepository;

    @SqlSetupAuthorBook
    @DisplayName("TEST findById - Should return existing Book")
    @Test
    void testFindById() {
        Optional<BookPojo> result = bookRepository.findById(1L);
        assertTrue(result.isPresent(), "Book with ID=1 should exist");

        BookPojo book = result.get();
        assertEquals(1L, book.getId());
        assertEquals("Book One by John", book.getTitle());
        assertEquals(LocalDate.of(2023, 1, 15), book.getReleaseDate());
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST findById - Should return empty Optional for non-existent Book")
    @Test
    void testFindByIdNotFound() {
        Optional<BookPojo> result = bookRepository.findById(999L);
        assertTrue(result.isEmpty(), "Book with ID=999 should not exist");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST findAll - Should return all Books")
    @Test
    void testFindAll() {
        Set<BookPojo> books = bookRepository.findAll();
        assertFalse(books.isEmpty(), "Expected some books in DB");
        assertEquals(4, books.size(), "Should find 4 books from test data");

        assertTrue(books.stream().anyMatch(b ->
                b.getId() == 1L
                        && "Book One by John".equals(b.getTitle())
                        && LocalDate.of(2023, 1, 15).equals(b.getReleaseDate())
        ));
    }

    @DisplayName("TEST findAll - Should return empty if no Books in DB")
    @Test
    void testFindAllEmpty() {
        Set<BookPojo> books = bookRepository.findAll();
        assertTrue(books.isEmpty(), "Expected no books in an empty DB");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST create - Should create and return BookPojo")
    @Test
    void testCreate() {
        var newBook = new BookPojo(null, 1L, "New Book", LocalDate.of(2023, 10, 10));

        BookPojo created = bookRepository.create(newBook);
        assertNotNull(created.getId(), "Newly created book should have an auto-generated ID");
        assertEquals("New Book", created.getTitle());
        assertEquals(LocalDate.of(2023, 10, 10), created.getReleaseDate());
    }

    @DisplayName("TEST create - Should fail for missing fields")
    @Test
    void testCreateWithMissingFields() {
        var incompleteBook = new BookPojo(null, null, null, null);

        assertThrows(Exception.class, () -> bookRepository.create(incompleteBook));
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST update - Should update and return BookPojo")
    @Test
    void testUpdate() {
        var bookUpdate = new BookPojo(
                1L,
                1L,
                "Updated Title",
                LocalDate.of(2023, 1, 15)
        );

        BookPojo updated = bookRepository.update(1L, bookUpdate);
        assertNotNull(updated, "Should return updated book");
        assertEquals(1L, updated.getId());
        assertEquals("Updated Title", updated.getTitle());
        assertEquals(LocalDate.of(2023, 1, 15), updated.getReleaseDate());
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST update - Should return null if Book not found")
    @Test
    void testUpdateNotFound() {
        BookPojo nonExistent = new BookPojo(
                null,
                1L,
                "NonExistent Book",
                LocalDate.of(2023, 1, 15)
        );

        BookPojo result = bookRepository.update(999L, nonExistent);
        assertNull(result, "Expected update to fail, returning null for non-existent book");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST delete - Should delete and return true")
    @Test
    void testDelete() {
        boolean isDeleted = bookRepository.delete(1L);
        assertTrue(isDeleted, "Book with ID=1 should be deleted");

        Optional<BookPojo> deleted = bookRepository.findById(1L);
        assertTrue(deleted.isEmpty(), "Deleted book should not be found");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST delete - Should return false if Book not found")
    @Test
    void testDeleteNotFound() {
        boolean isDeleted = bookRepository.delete(999L);
        assertFalse(isDeleted, "Deleting non-existent book should return false");
    }
}
