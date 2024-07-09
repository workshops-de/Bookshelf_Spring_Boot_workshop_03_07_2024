package de.workshops.bookshelf;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
class BookRepositoryNamedParameterJdbcTemplate {
    private final NamedParameterJdbcTemplate template;

    BookRepositoryNamedParameterJdbcTemplate(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    List<Book> findAllBooks() {
        var sql = "SELECT * FROM book";
        var books = template.query(sql, new BeanPropertyRowMapper<>(Book.class));
        return books;
    }

    Book saveBook(Book book) {
        var sql = "INSERT INTO book (title, author, isbn, description) VALUES (:title, :author, :isbn, :description)";
        var parameters = new MapSqlParameterSource();
        parameters.addValue("title", book.getTitle());
        parameters.addValue("author", book.getAuthor());
        parameters.addValue("isbn", book.getIsbn());
        parameters.addValue("description", book.getDescription());
        template.update(sql, parameters);
        return book;
    }
}
