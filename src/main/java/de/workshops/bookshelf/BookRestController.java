package de.workshops.bookshelf;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
class BookRestController {

    private final BookService bookService;

    BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("{isbn}")
    Book getBookByIsbn(@PathVariable(name = "isbn") String bookIsbn) {
        return bookService.getBookByIsbn(bookIsbn);
    }

    @GetMapping(params = "author")
    Book getBooksByAuthor(@RequestParam @Size(min = 3) String author) {
        return bookService.getBookByAuthor(author);
    }

    @PostMapping("/search")
    ResponseEntity<List<Book>> searchBooks(@RequestBody @Valid BookSearchRequest bookSearchRequest) {
        var searchResult = bookService.searchBook(bookSearchRequest);
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(searchResult);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    Book createBook(@RequestBody @Valid Book book) {
        var savedBook = bookService.createBook(book);
        return savedBook;
    }

    @ExceptionHandler
    ResponseEntity<String> handleBookException(BookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
