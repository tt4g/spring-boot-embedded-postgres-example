package spring.boot.embedded.postgres.example;

public interface UserService {

    int count();

    UserEntity addUser(String name);

}
