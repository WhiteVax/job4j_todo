package ru.job4j.todo.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.User;

import static org.assertj.core.api.Assertions.*;

class UserDBStoreTest {

    @AfterEach
    void after() {
        var session = new Main().sf().openSession();
        session.beginTransaction();
        session.createQuery("DELETE FROM User")
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void whenFindUserByLoginAndPassword() {
        var userStore = new UserDBStore(new Main().sf().openSession().getSessionFactory());
        userStore.createUser(new User(1, "Ivan", "login", "password"));
        var user = userStore.findUserByLoginAndPassword("login", "password");
        assertThat(user.get().getLogin()).isEqualTo("login");
    }
}