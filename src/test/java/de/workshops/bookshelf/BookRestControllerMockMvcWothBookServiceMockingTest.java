package de.workshops.bookshelf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookRestControllerMockMvcWothBookServiceMockingTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookService mockedBookService;

    @Captor
    ArgumentCaptor<String> isbnArgumentCaptor;

    @Test
    void getAllBooks() throws Exception {
        when(mockedBookService.getAllBooks()).thenReturn(List.of());

        var response = mockMvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();

        List<Book> allBooks = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(allBooks).isEmpty();
    }

    @Test
    void getBookByIsbn_withCheckThatCoorektISBNWasUsedToCallBookServiceMethod() throws Exception {
        // Mock preparation where argument value of call to BookService::getBookByIsbn
        // will be captured by an ArgumentCaptor
        when(mockedBookService.getBookByIsbn(isbnArgumentCaptor.capture())).thenReturn(new Book());

        var response = mockMvc.perform(get("/book/123456789"))
                .andExpect(status().isOk())
                .andReturn();

        var jsonPayload = response.getResponse().getContentAsString();
        Book book = objectMapper.readValue(jsonPayload, new TypeReference<>() {});

        assertThat(book).isNotNull();

        // check that the argument value which was used to call BookService::getBookByIsbn
        // is equal to the value from the http-Request
        var isbnArgument = isbnArgumentCaptor.getValue();
        assertThat(isbnArgument).isEqualTo("123456789");
    }

    @Test
    void searchBooks_expect_OK() throws Exception {
        when(mockedBookService.searchBook(any(BookSearchRequest.class))).thenReturn(List.of(new Book(), new Book()));
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
}