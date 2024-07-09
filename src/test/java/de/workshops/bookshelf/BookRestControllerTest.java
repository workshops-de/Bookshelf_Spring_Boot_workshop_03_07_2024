package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class BookRestControllerTest {

    @Autowired
    private BookRestController controller;

    @Test
    void getAllBooks() {
        var allBooks = controller.getAllBooks();

        assertThat(allBooks).hasSize(3);
    }

    @Test
    void getByIsbn_OK() {
        var existingIsbn = "978-3826655487";
        var bookByIsbn = controller.getBookByIsbn(existingIsbn);

        assertThat(bookByIsbn).isNotNull();
    }

    @Test
    void getByIsbn_Exception() {
        final String wrongBookIsbn = "978-3826655480";
        assertThatExceptionOfType(BookException.class).isThrownBy(() -> controller.getBookByIsbn(wrongBookIsbn));
    }
}