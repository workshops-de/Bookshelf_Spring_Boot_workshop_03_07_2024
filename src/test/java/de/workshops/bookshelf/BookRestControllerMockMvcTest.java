package de.workshops.bookshelf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(controllers = BookRestController.class)
//@Import({BookService.class, BookRepository.class})
@ActiveProfiles("test")
class BookRestControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @LocalServerPort
//    int port;

    @Test
    void getAllBooks() throws Exception {
        var response = mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();

        List<Book> allBooks = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(allBooks).hasSizeGreaterThanOrEqualTo(3);
    }

    @Test
    void getBookByIsbn_isbnExists() throws Exception {
        var response = mockMvc.perform(get("/book/978-0201633610"))
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();
        Book book = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(book).isNotNull();
    }

    @Test
    void getBookByIsbn_wrongIsbn() throws Exception {
        mockMvc.perform(get("/book/123456789"))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void getBookByAuthor_authorNameTooShort() throws Exception {
        mockMvc.perform(get("/book?author=Ro"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void searchBooks_expect_OK() throws Exception {
        var response = mockMvc.perform(
                        post("/book/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "author": "Rob",
                                  "isbn": "978-3836211161"
                                }""")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();

        List<Book> allBooks = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(allBooks).hasSize(2);
    }

    @Test
    void createBook_expect_OK() throws Exception {
        String isbn = "111-1111111111";
        String author = "Birgit Kratz";
        String title = "My first book";
        String description = "It's just genious.";

        var expectedBook = new Book();
        expectedBook.setIsbn(isbn);
        expectedBook.setAuthor(author);
        expectedBook.setTitle(title);
        expectedBook.setDescription(description);

        var response = mockMvc.perform(post("/book")
                        .content("""
                                {
                                    "isbn": "%s",
                                    "title": "%s",
                                    "author": "%s",
                                    "description": "%s"
                                }""".formatted(isbn, title, author, description))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();

        Book createdBook = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(createdBook).isNotNull();
        assertThat(createdBook).isEqualTo(expectedBook);
    }

}