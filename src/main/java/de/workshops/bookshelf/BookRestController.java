package de.workshops.bookshelf;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Validated
public class BookRestController {

    private final ObjectMapper mapper;

    private final ResourceLoader resourceLoader;

    private List<Book> books;

    public BookRestController(ObjectMapper mapper, ResourceLoader resourceLoader) {
        this.mapper = mapper;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws Exception {
        final var resource = resourceLoader.getResource("classpath:books.json");
        this.books = mapper.readValue(resource.getInputStream(), new TypeReference<>() {});
    }

    @GetMapping
    List<Book> getAllBooks() {
        return books;
    }

    @GetMapping("{isbn}")
    Book getBookByIsbn(@PathVariable(name = "isbn") String bookIsbn) {
        var book = books.stream()
                .filter(b -> b.getIsbn().equals(bookIsbn))
                .findFirst()
                .orElseThrow(() -> new BookException("Could not find book with ISBN %s".formatted(bookIsbn)));
        return book;
    }

    @GetMapping(params = "author")
    Book getBooksByAuthor(@RequestParam @Size(min = 3) String author) {
        var book = books.stream()
                .filter(b -> b.getAuthor().contains(author))
                .findFirst()
                .orElseThrow(() -> new BookException("Could not find book with author containing %s".formatted(author)));
        return book;
    }

    @PostMapping("/search")
    ResponseEntity<List<Book>> searchBooks(@RequestBody @Valid BookSearchRequest bookSearchRequest) {
        var searchResult =  books.stream()
                .filter(b -> b.getAuthor().contains(bookSearchRequest.author()) || b.getIsbn().equals(bookSearchRequest.isbn()))
                .toList();
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(searchResult);
    }

    @ExceptionHandler
    ResponseEntity<String> handleBookException(BookException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
