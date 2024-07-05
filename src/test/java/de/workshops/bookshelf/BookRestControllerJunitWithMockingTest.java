package de.workshops.bookshelf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRestControllerJunitWithMockingTest {

    @Mock
    private BookService mockedBookService;

    @InjectMocks                                     // injects mockedBookService into BookRestController (constructor injection)
    private BookRestController bookRestController;  // class under Test

    @Test
    void getAllBooks() {
        // configure behaviour of mockedBookService for this test
        // return an empty List of Books, when 'getAllBooks' on BookService is called
        when(mockedBookService.getAllBooks()).thenReturn(List.of());

        var allBooks = bookRestController.getAllBooks();

        assertThat(allBooks).isEmpty();
    }

    @Test
    void getBookByIsbn_OK() {
        // configure behaviour of mockedBookService for this test
        // return a Book, when 'getBookByIsbn' on BookService is called with any isbn
        when(mockedBookService.getBookByIsbn(anyString())).thenReturn(new Book());

        var bookByIsbn = bookRestController.getBookByIsbn("an isbn");

        assertThat(bookByIsbn).isNotNull();
    }

    @Test
    void getBookByIsbn_bookServiceThrowsBookException() {
        // configure behaviour of mockedBookService for this test
        // BookService throws a BookException when 'getBookByIsbn' is called with any isbn
        doThrow(BookException.class).when(mockedBookService).getBookByIsbn(anyString());

        // test that BookRestController receives the BookException
        assertThatExceptionOfType(BookException.class).isThrownBy(() -> bookRestController.getBookByIsbn("an isbn"));
    }

    // Eigentlich wuerde man eventuell erwarten, dass auch hier die Size-Validierung
    // von der Controller Methode greift. Da wir uns hier aber in einem reinen JUnit Test befinden,
    // wurde keine Applikation gestartet, somit auch kein Application-Context aufgebaut und damit
    // auch der Validierungsmechanismus nicht aktiviert.
    @Test
    void getBookByAuthor_authorNameHasOnly2Letters() {
        when(mockedBookService.getBookByAuthor(anyString())).thenReturn(new Book());

        var bookByAuthor = bookRestController.getBooksByAuthor("Bi");

        assertThat(bookByAuthor).isNotNull();
    }
}