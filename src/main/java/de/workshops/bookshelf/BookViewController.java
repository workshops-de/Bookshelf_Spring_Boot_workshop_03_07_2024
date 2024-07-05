package de.workshops.bookshelf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
class BookViewController {

    private final BookService bookService;

    BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    String viewAllBook(Model model) {
        var allBooks = bookService.getAllBooks();
        model.addAttribute("books", allBooks);

        return "booklist";
    }
}
