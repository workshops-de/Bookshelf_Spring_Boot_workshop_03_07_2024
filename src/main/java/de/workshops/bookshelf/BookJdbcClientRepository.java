package de.workshops.bookshelf;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookJdbcClientRepository {
    private final JdbcClient client;

    public BookJdbcClientRepository(JdbcClient client) {
        this.client = client;
    }

    List<Book> findAllBooks() {
        var sql = "SELECT * FROM book";
        var books = client.sql(sql)
                .query(new BeanPropertyRowMapper<>(Book.class))
                .list();
        return books;
    }

    Book saveBook(Book book) {
        var sql = "INSERT INTO book (title, author, isbn, description) VALUES (?, ?, ?, ?)";
        client.sql(sql)
                .params(book.getTitle(), book.getAuthor(), book.getIsbn(), book.getDescription())
                .update();
        return book;
    }
}
