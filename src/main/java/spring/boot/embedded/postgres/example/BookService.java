package spring.boot.embedded.postgres.example;

public interface BookService {

    int count();

    BookEntity addBook(String name);

}
