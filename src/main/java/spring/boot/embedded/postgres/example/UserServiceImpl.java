package spring.boot.embedded.postgres.example;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final Jdbi jdbi;

    @Autowired
    public UserServiceImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Transactional // Spring management transaction.
    @Override
    public int count() {
        Handle handle = this.jdbi.open();

        int count = handle.createQuery("SELECT COUNT (*) FROM user")
            .mapTo(int.class)
            .one();

        return count;
    }

    @Override
    public UserEntity addUser(String name) {
        // JDBI management transaction.
        UserEntity userEntity = this.jdbi.inTransaction(TransactionIsolationLevel.READ_COMMITTED, handle -> {
            return handle.createUpdate("INSERT INTO user (name) VALUES(:name)")
                .bind("name", name)
                .executeAndReturnGeneratedKeys("id")
                .map((resultSet, context) -> {
                    // GeneratedKeys and access result set all attributes only supports PostgreSQL.
                    // See: https://jdbi.org/#_generated_keys
                    int persistedId = resultSet.getInt("id");
                    String persistedName = resultSet.getString("name");

                    return new UserEntity(persistedId, persistedName);
                })
                .one();
        });

        return userEntity;
    }

}
