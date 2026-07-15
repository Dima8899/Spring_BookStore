package mate.academy.library.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import mate.academy.library.dao.BookRepository;
import mate.academy.library.dao.CategoryRepository;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.model.Book;
import mate.academy.library.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional // откатывает изменения в БД после каждого теста
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Book savedBook;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setName("Fiction");
        category = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Book One");
        book.setAuthor("Author One");
        book.setIsbn("111-111");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(new HashSet<>(Set.of(category)));
        savedBook = bookRepository.save(book);
    }

    @Test
    @DisplayName("GET /books returns a page of books")
    @WithMockUser(roles = "USER")
    void getAll_booksExist_returnsPageOfBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Book One"));
    }

    @Test
    @DisplayName("GET /books/{id} existing id returns the book")
    @WithMockUser(roles = "USER")
    void getBookById_existingId_returnsBook() throws Exception {
        mockMvc.perform(get("/books/{id}", savedBook.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedBook.getId()))
                .andExpect(jsonPath("$.title").value("Book One"));
    }

    @Test
    @DisplayName("GET /books/{id} non-existing id returns 404")
    @WithMockUser(roles = "USER")
    void getBookById_nonExistingId_returnsNotFound() throws Exception {
        long nonExistingId = savedBook.getId() + 1000;

        mockMvc.perform(get("/books/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Can't find book by id: " + nonExistingId));
    }

    @Test
    @DisplayName("POST /books valid request returns 201 and created book")
    @WithMockUser(roles = "ADMIN")
    void createBook_validRequest_returnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Book Two");
        requestDto.setAuthor("Author Two");
        requestDto.setIsbn("222-222");
        requestDto.setPrice(BigDecimal.valueOf(24.99));
        requestDto.setCategoryIds(Set.of(category.getId()));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Book Two"));

        // given/then через реальную БД, а не мок
        boolean exists = bookRepository.findAll().stream()
                .anyMatch(b -> b.getTitle().equals("Book Two"));
        org.assertj.core.api.Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("POST /books blank title returns 400")
    @WithMockUser(roles = "ADMIN")
    void createBook_blankTitle_returnsBadRequest() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("333-333");
        requestDto.setPrice(BigDecimal.valueOf(19.99));
        requestDto.setCategoryIds(Set.of(category.getId()));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /books/{id} valid request returns updated book")
    @WithMockUser(roles = "ADMIN")
    void updateBook_validRequest_returnsUpdatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("Updated Title");
        requestDto.setAuthor("Author One");
        requestDto.setIsbn("111-111");
        requestDto.setPrice(BigDecimal.valueOf(29.99));
        requestDto.setCategoryIds(Set.of(category.getId()));

        mockMvc.perform(put("/books/{id}", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    @DisplayName("DELETE /books/{id} returns 204 and removes the book")
    @WithMockUser(roles = "ADMIN")
    void deleteBook_validId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/{id}", savedBook.getId()))
                .andExpect(status().isNoContent());

        org.assertj.core.api.Assertions.assertThat(
                bookRepository.findById(savedBook.getId())).isEmpty();
    }

    @Test
    @DisplayName("GET /books/search returns matching books")
    @WithMockUser(roles = "USER")
    void searchBooks_validParameters_returnsMatchingBooks() throws Exception {
        mockMvc.perform(get("/books/search").param("author", "Author One"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].author").value("Author One"));
    }
}
