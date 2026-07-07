package mate.academy.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.library.dao.CategoryRepository;
import mate.academy.library.dto.CategoryDto;
import mate.academy.library.dto.CreateCategoryDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CategoryMapper;
import mate.academy.library.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fantasy");
        category.setDescription("Fantasy books");

        categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Fantasy");
        categoryDto.setDescription("Fantasy books");
    }

    @Test
    @DisplayName("findAll() returns list of CategoryDto mapped from repository result")
    void findAll_categoriesExist_returnsListOfCategoryDto() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> result = categoryService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Fantasy");
    }

    @Test
    @DisplayName("getById() existing id returns CategoryDto")
    void getById_existingId_returnsCategoryDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Fantasy");
    }

    @Test
    @DisplayName("getById() non-existing id throws EntityNotFoundException")
    void getById_nonExistingId_throwsEntityNotFoundException() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(100L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("100");
    }

    @Test
    @DisplayName("save() valid request returns saved CategoryDto")
    void save_validRequest_returnsCategoryDto() {
        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("Fantasy");
        requestDto.setDescription("Fantasy books");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertThat(result.getName()).isEqualTo("Fantasy");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("update() existing id updates and returns CategoryDto")
    void update_existingId_returnsUpdatedCategoryDto() {
        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("Updated Name");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.update(1L, requestDto);

        assertThat(result).isNotNull();
        verify(categoryMapper, times(1)).updateCategoryFromDto(requestDto, category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("update() non-existing id throws EntityNotFoundException")
    void update_nonExistingId_throwsEntityNotFoundException() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        CreateCategoryDto requestDto = new CreateCategoryDto();
        requestDto.setName("Doesn't matter");

        assertThatThrownBy(() -> categoryService.update(100L, requestDto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteById() delegates to repository")
    void deleteById_validId_callsRepositoryDeleteById() {
        categoryService.deleteById(1L);

        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
