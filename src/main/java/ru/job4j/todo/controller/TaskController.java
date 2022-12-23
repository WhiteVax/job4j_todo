package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

import static ru.job4j.todo.util.UserSession.getUser;

@Controller
public class TaskController {

    private final TaskService store;

    public TaskController(TaskService store) {
        this.store = store;
    }

    @GetMapping("")
    public String main(Model model, HttpSession session) {
        var user = getUser(session);
        model.addAttribute("user", user);
        return "task/index";
    }

    @GetMapping("/tasks")
    public String allTasks(Model model, HttpSession session) {
        model.addAttribute("tasks", store.findAllTask());
        model.addAttribute("user", getUser(session));
        return "task/tasks";
    }

    @GetMapping("/new")
    public String newTask(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("tasks", store.findNewTask());
        return "task/new";
    }

    @GetMapping("/done")
    public String doneTask(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("tasks", store.findOldTask());
        return "task/done";
    }

    @GetMapping("/tasks/{id}")
    public String getTask(Model model, @PathVariable("id") int id, HttpSession session) {
        model.addAttribute("user", getUser(session));
        var task = store.findById(id).get();
        model.addAttribute("task", task);
        return "task/task";
    }

    @GetMapping("/tasks/add")
    public String createTask(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("task", model);
        return "task/formAdd";
    }

    @PostMapping("/create")
    public String addTask(Model model, @ModelAttribute Task task, HttpSession session) {
        model.addAttribute("user", getUser(session));
        store.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/update/{id}")
    public String formUpdateTask(Model model, @PathVariable("id") int id, HttpSession session) {
        model.addAttribute("user", getUser(session));
        var task = store.findById(id).get();
        model.addAttribute("task", task);
        return "task/formUpdate";
    }

    @PostMapping("/update")
    public String updateTask(Model model, @ModelAttribute Task task, HttpSession session) {
        model.addAttribute("user", getUser(session));
        store.updateTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        store.deleteTask(id);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/completed/{id}")
    public String completedTask(Model model, @ModelAttribute Task task, HttpSession session) {
        model.addAttribute("user", getUser(session));
        task.setDone(true);
        store.completedTask(task);
        return "redirect:/tasks";
    }
}
