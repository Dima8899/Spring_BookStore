package mate.academy.library.mapper;

import mate.academy.library.dto.BookDto;
import mate.academy.library.dto.CreateBookRequestDto;
import mate.academy.library.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
