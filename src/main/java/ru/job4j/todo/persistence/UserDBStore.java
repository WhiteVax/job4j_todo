package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

/**
 * Хранилище данных пользователя
 */
@Repository
@Slf4j
@AllArgsConstructor
public class UserDBStore {

    private final SessionFactory sf;
    private final CrudRepository crudRepository;

    /**
     * Метод для регистрации пользователя
     */
    public Optional<User> createUser(User user) {
        try {
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            log.error("Error in createUser.", e);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    /**
     * Метод возвращает с хранилища пользователя по логину и паролю
     */
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return crudRepository.optional("FROM User WHERE login = :fLogin AND password = :fPass",
                User.class,
                Map.of("fLogin", login,
                        "fPass", password));
    }
}
