package mate.academy.library.mapper;

import mate.academy.library.dto.CategoryDto;
import mate.academy.library.dto.CreateCategoryDto;
import mate.academy.library.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CreateCategoryDto createCategoryDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(CreateCategoryDto dto, @MappingTarget Category category);
}
