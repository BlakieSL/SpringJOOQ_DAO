package org.example.springjooq;

import org.example.jooq.generated.tables.pojos.AuthorPojo;
import org.example.springjooq.repository.declaration.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the AuthorRepository (jOOQ-based)
 * using the same database/test data setup as your previous project.
 */
@SpringBootTest
public class AuthorRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorRepositoryTest.class);

    @Autowired
    private AuthorRepository authorRepository;


    @SqlSetupAuthorBook
    @DisplayName("TEST findById - Should return an existing Author")
    @Test
    void testFindById() {
        var result = authorRepository.findById(1L);
        assertTrue(result.isPresent(), "Author with ID=1 should exist");

        var author = result.get();
        assertEquals(1L, author.getId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST findById - Should return Optional.empty() for non-existent author")
    @Test
    void testFindByIdNotFound() {
        var result = authorRepository.findById(999L);
        assertTrue(result.isEmpty(), "Author with ID=999 should not exist");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST findAll - Should return all Authors (no association checks)")
    @Test
    void testFindAll() {
        Set<AuthorPojo> result = authorRepository.findAll();
        assertFalse(result.isEmpty(), "Expected some authors in DB");
        assertEquals(3, result.size(), "Should have 3 authors from test data");

        assertTrue(result.stream().anyMatch(a ->
                a.getId() == 1L &&
                        "John".equals(a.getFirstName()) &&
                        "Doe".equals(a.getLastName())
        ));
    }

    @DisplayName("TEST findAll - Should return empty Set when DB has no authors")
    @Test
    void testFindAllEmpty() {
        Set<AuthorPojo> result = authorRepository.findAll();
        assertTrue(result.isEmpty(), "Expected no authors in an empty DB");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST create - Should create and return AuthorPojo")
    @Test
    void testCreate() {
        var newAuthor = new AuthorPojo(null, "Alice", "Walker");
        var createdAuthor = authorRepository.create(newAuthor);

        assertNotNull(createdAuthor.getId(), "Newly created author should have an ID");
        assertEquals("Alice", createdAuthor.getFirstName());
        assertEquals("Walker", createdAuthor.getLastName());
    }

    @DisplayName("TEST create - Should fail for missing fields")
    @Test
    void testCreateWithMissingFields() {
        var incompleteAuthor = new AuthorPojo(null, null, "Walker");

        assertThrows(Exception.class, () -> authorRepository.create(incompleteAuthor));
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST update - Should update and return AuthorPojo")
    @Test
    void testUpdate() {
        var existingAuthor = new AuthorPojo(1L, "UpdatedFirstName", "UpdatedLastName");
        var updatedAuthor = authorRepository.update(1L, existingAuthor);

        assertNotNull(updatedAuthor, "Should return updated author");
        assertEquals(1L, updatedAuthor.getId());
        assertEquals("UpdatedFirstName", updatedAuthor.getFirstName());
        assertEquals("UpdatedLastName", updatedAuthor.getLastName());
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST update - Should throw exception or return null when Author not found")
    @Test
    void testUpdateNotFound() {
        var nonExistentAuthor = new AuthorPojo(999L, "NonExistent", "Author");

        var updatedAuthor = authorRepository.update(999L, nonExistentAuthor);
        assertNull(updatedAuthor);
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST delete - Should delete and return true")
    @Test
    void testDelete() {
        boolean isDeleted = authorRepository.delete(1L);
        assertTrue(isDeleted, "Author with ID=1 should be deleted");

        Optional<AuthorPojo> deletedAuthor = authorRepository.findById(1L);
        assertTrue(deletedAuthor.isEmpty(), "Deleted author should not be found");
    }

    @SqlSetupAuthorBook
    @DisplayName("TEST delete - Should return false when Author not found")
    @Test
    void testDeleteNotFound() {
        boolean isDeleted = authorRepository.delete(999L);
        assertFalse(isDeleted, "Deleting a non-existent author should return false");
    }
}
