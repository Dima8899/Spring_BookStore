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
import mate.academy.library.dto.BookDtoWithoutCategoryIds;
import mate.academy.library.dto.CategoryDto;
import mate.academy.library.dto.CreateCategoryDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.security.JwtUtil;
import mate.academy.library.service.BookService;
import mate.academy.library.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fantasy");
        categoryDto.setDescription("Fantasy books");
    }

    @Test
    @DisplayName("POST /categories valid request returns 201 and created category")
    void createCategory_validRequest_returnsCreatedCategory() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy books");

        when(categoryService.save(any(CreateCategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("POST /categories blank name returns 400")
    void createCategory_blankName_returnsBadRequest() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /categories returns list of categories")
    void getAll_categoriesExist_returnsListOfCategories() throws Exception {
        when(categoryService.findAll()).thenReturn(List.of(categoryDto));

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} existing id returns the category")
    void getCategoryById_existingId_returnsCategory() throws Exception {
        when(categoryService.getById(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    @DisplayName("GET /categories/{id} non-existing id returns 404")
    void getCategoryById_nonExistingId_returnsNotFound() throws Exception {
        when(categoryService.getById(100L))
                .thenThrow(new EntityNotFoundException("Can't find category by id: 100"));

        mockMvc.perform(get("/categories/{id}", 100L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Can't find category by id: 100"));
    }

    @Test
    @DisplayName("PUT /categories/{id} valid request returns updated category")
    void updateCategory_validRequest_returnsUpdatedCategory() throws Exception {
        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("Updated Name");

        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(1L);
        updatedDto.setName("Updated Name");

        when(categoryService.update(anyLong(), any(CreateCategoryDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("DELETE /categories/{id} returns 204 and delegates to service")
    void deleteCategory_validId_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("GET /categories/{id}/books returns books of the category")
    void getBooksByCategoryId_existingCategory_returnsBooks() throws Exception {
        BookDtoWithoutCategoryIds book = new BookDtoWithoutCategoryIds();
        book.setId(1L);
        book.setTitle("Book One");
        book.setPrice(BigDecimal.valueOf(19.99));

        when(bookService.getBooksByCategoriesId(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/categories/{id}/books", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Book One"));
    }
}
