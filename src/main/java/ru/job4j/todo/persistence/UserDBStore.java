package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

/**
 * Хранилище данных пользователя
 */
@Repository
@Slf4j
@AllArgsConstructor
public class UserDBStore {

    private final SessionFactory sf;

    public Optional<User> createUser(User user) {
        try {
            var session = sf.openSession();
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error in createUser.", e);
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        Optional<User> user = Optional.empty();
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var query = session.createQuery("FROM User WHERE login = :fLogin AND password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password);
            user = query.uniqueResultOptional();
        } catch (Exception e) {
            log.error("Error HQL in findUserByEmailAndPassword.", e);
            session.getTransaction().rollback();
        }
        return user;
    }
}
