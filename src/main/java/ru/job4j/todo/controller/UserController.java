package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import java.util.Optional;


@Controller
@AllArgsConstructor
public class UserController {
    private final UserService store;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", model);
        return "user/registration";
    }

    @PostMapping("/createUser")
    public String regOrFail(Model model, @ModelAttribute User user) {
        Optional<User> regUser = store.createUser(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message",
                    "Пользователь с таким логином уже зарегистрирован.");
            return "redirect:/fail";
        }
        return "redirect:/successfully";
    }

    @GetMapping("/fail")
    public String failReg() {
        return "user/fail";
    }

    @GetMapping("/successfully")
    public String successfullyReg() {
        return "user/successfully";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "user/loginPage";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        Optional<User> rsl = store.userFindAtLoginAndPassword(
                user.getLogin(), user.getPassword()
        );
        if (rsl.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        return "redirect:/";
    }
}
