package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.TimeZone;

import static ru.job4j.todo.util.UserSession.getUser;
import static ru.job4j.todo.util.TaskZone.getAllZone;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserService store;

    @GetMapping("/registration")
    public String registration(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("timeZone", getAllZone());
        return "user/registration";
    }

    @PostMapping("/createUser")
    public String regOrFail(@RequestParam("timeZone") String zone, Model model, @ModelAttribute User user) {

        user.setUserZone(TimeZone.getTimeZone(zone));
        Optional<User> regUser = store.createUser(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message",
                    "Пользователь с таким логином уже зарегистрирован.");
            return "redirect:/fail";
        }
        return "redirect:/successfully";
    }

    @GetMapping("/fail")
    public String failReg(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        return "user/fail";
    }

    @GetMapping("/successfully")
    public String successfullyReg(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        return "user/successfully";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail,
                            HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("fail", fail != null);
        return "user/loginPage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> rsl = store.userFindAtLoginAndPassword(
                user.getLogin(), user.getPassword()
        );
        if (rsl.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        var session = req.getSession();
        session.setAttribute("user", rsl.get());
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
