package spring.boot.embedded.postgres.example;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = DatabaseProvider.ZONKY)
@FlywayTest
public class SimpleTest {

    private final BookService bookService;

    @Autowired
    public SimpleTest(BookService bookService) {
        this.bookService = bookService;
    }

    @Test
    @FlywayTest(locationsForMigrate = "flyway/SimpleTest/count")
    public void count() {
        int count = this.bookService.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void addBook() {
        String name = "John Doe";

        BookEntity bookEntity = this.bookService.addBook(name);

        assertThat(bookEntity)
            .isNotNull()
            .satisfies(bookEntity_ -> assertThat(bookEntity_.getName()).isEqualTo(name));
    }

}
