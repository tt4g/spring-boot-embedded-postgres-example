package spring.boot.embedded.postgres.example;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService {

    private final Jdbi jdbi;

    @Autowired
    public BookServiceImpl(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Transactional // Spring management transaction.
    @Override
    public int count() {
        Handle handle = this.jdbi.open();

        int count = handle.createQuery("SELECT COUNT (*) FROM book")
            .mapTo(int.class)
            .one();

        return count;
    }

    @Override
    public BookEntity addBook(String name) {
        // JDBI management transaction.
        int id = this.jdbi.inTransaction(TransactionIsolationLevel.READ_COMMITTED, handle -> {
            return handle.createUpdate("INSERT INTO book (name) VALUES(:name)")
                .bind("name", name)
                .executeAndReturnGeneratedKeys("id")
                .map((resultSet, context) -> {
                    int persistedId = resultSet.getInt("id");

                    return persistedId;
                })
                .one();
        });

        return new BookEntity(id, name);
    }

}
