package mate.academy.library.service;

import java.util.List;
import mate.academy.library.dto.CategoryDto;
import mate.academy.library.dto.CreateCategoryDto;

public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryDto CreatecategoryDto);

    CategoryDto update(Long id, CreateCategoryDto CreateCategoryDto);

    void deleteById(Long id);
}
