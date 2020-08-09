package spring.boot.embedded.postgres.example;

import java.util.Objects;

public class BookEntity {

    private final int id;

    private final String name;

    public BookEntity(int id, String name) {
        this.id = id;
        this.name = Objects.requireNonNull(name);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookEntity that = (BookEntity) o;
        return id == that.id &&
            name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
