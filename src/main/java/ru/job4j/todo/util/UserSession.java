package ru.job4j.todo.util;

import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

public final class UserSession {
    private UserSession() {
    }

    public static User getUser(HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
