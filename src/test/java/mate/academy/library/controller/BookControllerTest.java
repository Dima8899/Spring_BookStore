package mate.academy.library.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.security.JwtUtil;
import mate.academy.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Book One");
        bookDto.setAuthor("Author One");
        bookDto.setIsbn("111-111");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setCategoryIds(Set.of(1L));
    }

    @Test
    @DisplayName("GET /books returns a page of books")
    void getAll_booksExist_returnsPageOfBooks() throws Exception {
        when(bookService.findAll(any()))
                .thenReturn(new PageImpl<>(List.of(bookDto), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Book One"));
    }

    @Test
    @DisplayName("GET /books/{id} existing id returns the book")
    void getBookById_existingId_returnsBook() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(bookDto);

        mockMvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Book One"));
    }

    @Test
    @DisplayName("GET /books/{id} non-existing id returns 404")
    void getBookById_nonExistingId_returnsNotFound() throws Exception {
        when(bookService.findBookById(100L))
                .thenThrow(new EntityNotFoundException("Can't find book by id: 100"));

        mockMvc.perform(get("/books/{id}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Can't find book by id: 100"));
    }

    @Test
    @DisplayName("POST /books valid request returns 201 and created book")
    void createBook_validRequest_returnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book One");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("111-111");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCategoryIds(Set.of(1L));

        when(bookService.createBook(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Book One"));
    }

    @Test
    @DisplayName("POST /books blank title returns 400")
    void createBook_blankTitle_returnsBadRequest() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("111-111");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCategoryIds(Set.of(1L));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /books/{id} valid request returns updated book")
    void updateBook_validRequest_returnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("111-111");
        requestDto.setPrice(BigDecimal.valueOf(29.99));
        requestDto.setCategoryIds(Set.of(1L));

        BookDto updatedDto = new BookDto();
        updatedDto.setId(1L);
        updatedDto.setTitle("Updated Title");

        when(bookService.updateBook(anyLong(), any(CreateBookRequestDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/books/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @DisplayName("DELETE /books/{id} returns 204 and delegates to service")
    void deleteBook_validId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    @DisplayName("GET /books/search returns matching books")
    void searchBooks_validParameters_returnsMatchingBooks() throws Exception {
        when(bookService.searchBooks(any(), any()))
                .thenReturn(new PageImpl<>(List.of(bookDto), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/books/search").param("author", "Author One"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].author").value("Author One"));
    }
}
