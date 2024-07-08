package de.workshops.bookshelf;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class BookService {

    private final BookRepository bookRepository;

    BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    Book getBookByIsbn(String isbn) {
        var book = bookRepository.findAllBooks().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new BookException("Could not find book with ISBN %s".formatted(isbn)));
        return book;
    }

    Book getBookByAuthor(String author) {
        var book = bookRepository.findAllBooks().stream()
                .filter(b -> b.getAuthor().contains(author))
                .findFirst()
                .orElseThrow(() -> new BookException("Could not find book with author containing %s".formatted(author)));
        return book;
    }

    List<Book> searchBook(BookSearchRequest searchRequest) {
        var searchResult =  bookRepository.findAllBooks().stream()
                .filter(b -> b.getAuthor().contains(searchRequest.author()) || b.getIsbn().equals(searchRequest.isbn()))
                .toList();
        return searchResult;
    }


    Book createBook(Book book) {
        return bookRepository.saveBook(book);
    }
}
