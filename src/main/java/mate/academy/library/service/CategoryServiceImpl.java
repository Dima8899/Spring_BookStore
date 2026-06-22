package mate.academy.library.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dao.CategoryRepository;
import mate.academy.library.dto.CategoryDto;
import mate.academy.library.dto.CreateCategoryDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CategoryMapper;
import mate.academy.library.model.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: "
                        + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryDto CreateCategoryDto) {
        Category category = categoryMapper.toEntity(CreateCategoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryDto createCategoryDto) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: "
                        + id));
        Category category = categoryMapper.toEntity(createCategoryDto);
        category.setId(id);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
