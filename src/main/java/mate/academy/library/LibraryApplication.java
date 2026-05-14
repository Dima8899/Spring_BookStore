package mate.academy.library;

import java.math.BigDecimal;
import mate.academy.library.model.Book;
import mate.academy.library.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setTitle("Tom Soyer");
            book.setAuthor("Mark Twen");
            book.setIsbn("363728");
            book.setPrice(BigDecimal.valueOf(199));

            bookService.save(book);

            System.out.println(bookService.findAll());
        };
    }
}
