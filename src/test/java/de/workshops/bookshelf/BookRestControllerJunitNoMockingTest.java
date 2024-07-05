package de.workshops.bookshelf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;

class BookRestControllerJunitNoMockingTest {

    @Test
    void getAllBooks() throws Exception {
        // Instantiate and initialize all dependencies for BookRestController (class under test)
        var bookRepository = new BookRepository(new ObjectMapper(), new DefaultResourceLoader());
        bookRepository.init();
        var bookService = new BookService(bookRepository);

        // Instantiate class under test
        var bookRestController = new BookRestController(bookService);

        var allBooks = bookRestController.getAllBooks();

        assertThat(allBooks).hasSize(3);
    }
}