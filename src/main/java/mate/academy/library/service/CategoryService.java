package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.CategoryDto;

    public interface CategoryService {
        List<CategoryDto> findAll();
        CategoryDto getById(Long id);
        CategoryDto save(CategoryDto categoryDto);
        CategoryDto update(Long id, CategoryDto categoryDto);
        void deleteById(Long id);
    }
