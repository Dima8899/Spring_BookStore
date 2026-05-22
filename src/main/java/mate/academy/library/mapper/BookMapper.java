package mate.academy.library.mapper;

import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto dto, @MappingTarget Book book);
}
