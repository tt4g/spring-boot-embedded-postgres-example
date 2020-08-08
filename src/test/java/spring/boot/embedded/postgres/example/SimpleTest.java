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

    private final UserService userService;

    @Autowired
    public SimpleTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    @FlywayTest(locationsForMigrate = "flyway/SimpleTest/count")
    public void count() {
        int count = this.userService.count();

        assertThat(count).isEqualTo(3);
    }

    @Test
    public void addUser() {
        String name = "John Doe";

        UserEntity userEntity = this.userService.addUser(name);

        assertThat(userEntity)
            .isNotNull()
            .satisfies(userEntity_ -> assertThat(userEntity_.getName()).isEqualTo(name));
    }

}
