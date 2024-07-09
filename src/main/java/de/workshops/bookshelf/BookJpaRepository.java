package de.workshops.bookshelf;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookJpaRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);

    List<Book> findByAuthorOrDescriptionContaining(String author, String description);

    // JPQL Quere
    @Query("select b from Book b where b.author = ?1 or b.isbn like ?2")
    List<Book> searchBook(String author, String isbn);

    // here the named query from Book class will be used
    List<Book> searchBooksByDescription(@Param("descr") String description);

    // Native SQL Query
    @Query(nativeQuery = true, value = "SELECT count(*) FROM book WHERE author=?1")
    int countBooksByAuthor(String author);
}
