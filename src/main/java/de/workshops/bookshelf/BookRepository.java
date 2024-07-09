package de.workshops.bookshelf;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BookRepository {
    private final JdbcTemplate template;

    BookRepository(JdbcTemplate template) {
        this.template = template;
    }

    List<Book> findAllBooks() {
        var sql = "SELECT * FROM book";
        var books = template.query(sql, new BeanPropertyRowMapper<>(Book.class));
        return books;
    }

    Book saveBook(Book book) {
        var sql = "INSERT INTO book (title, author, isbn, description) VALUES (?, ?, ?, ?)";
        template.update(sql, book.getTitle(), book.getAuthor(), book.getIsbn(), book.getDescription());
        return book;
    }
}
