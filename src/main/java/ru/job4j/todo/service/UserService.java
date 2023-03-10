package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.persistence.UserDBStore;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserDBStore store;

    public Optional<User> createUser(User user) {
        return store.createUser(user);
    }

    public Optional<User> userFindAtLoginAndPassword(String login, String password) {
        return store.findUserByLoginAndPassword(login, password);
    }
}
