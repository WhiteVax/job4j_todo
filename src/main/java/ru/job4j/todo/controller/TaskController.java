package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import ru.job4j.todo.util.TaskTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static ru.job4j.todo.util.UserSession.getUser;
import static ru.job4j.todo.util.TaskTimeZone.setTimeWithTimeZone;

@Controller
public class TaskController {

    private final TaskService store;
    private final PriorityService storePriority;
    private final CategoryService storeCategory;

    public TaskController(TaskService store, PriorityService storePriority, CategoryService storeCategory) {
        this.store = store;
        this.storePriority = storePriority;
        this.storeCategory = storeCategory;
    }

    @GetMapping("")
    public String main(Model model, HttpSession session) {
        var user = getUser(session);
        model.addAttribute("user", user);
        return "task/index";
    }

    @GetMapping("/tasks")
    public String allTasks(Model model, HttpSession session) {
        List<Task> list = store.findAllTask().stream().map(TaskTimeZone::setTimeWithTimeZone).toList();
        model.addAttribute("tasks", list);
        model.addAttribute("user", getUser(session));
        return "task/tasks";
    }

    @GetMapping("/new")
    public String newTask(Model model, HttpSession session) {
        List<Task> list = store.findNewTask().stream().map(TaskTimeZone::setTimeWithTimeZone).toList();
        model.addAttribute("user", getUser(session));
        model.addAttribute("tasks", list);
        return "task/new";
    }

    @GetMapping("/done")
    public String doneTask(Model model, HttpSession session) {
        List<Task> list = store.findOldTask().stream().map(TaskTimeZone::setTimeWithTimeZone).toList();
        model.addAttribute("user", getUser(session));
        model.addAttribute("tasks", list);
        return "task/done";
    }

    @GetMapping("/tasks/{id}")
    public String getTask(Model model, @PathVariable("id") int id, HttpSession session) {
        model.addAttribute("user", getUser(session));
        var task =   setTimeWithTimeZone(store.findById(id).get());
        model.addAttribute("task", task);
        return "task/task";
    }

    @GetMapping("/tasks/add")
    public String createTask(Model model, HttpSession session) {
        User user = getUser(session);
        model.addAttribute("user", user);
        model.addAttribute("task", model);
        model.addAttribute("priorities", storePriority.getAll());
        model.addAttribute("categories", storeCategory.getAll());
        return "task/formAdd";
    }

    @PostMapping("/create")
    public String addTask(HttpServletRequest req, Model model,
                          @ModelAttribute Task task, HttpSession session) {
        User user = getUser(session);
        model.addAttribute("user", user);
        task.setUser(user);
        String[] array = req.getParameterValues("category.id");
        if (!store.createTask(task, array)) {
            model.addAttribute("message", "Ошибка при добавлении задачи.");
            return "task/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/update/{id}")
    public String formUpdateTask(Model model, @PathVariable("id") int id, HttpSession session) {
        var task = store.findById(id);
        model.addAttribute("user", getUser(session));
        if (task.isEmpty()) {
            model.addAttribute("message", "Данной задачи не существует.");
            return "task/404";
        }
        model.addAttribute("priorities", storePriority.getAll());
        model.addAttribute("task", task.get());
        model.addAttribute("categories", storeCategory.getAll());
        return "task/formUpdate";
    }

    @PostMapping("/update")
    public String updateTask(HttpServletRequest req, @ModelAttribute Task task,
                             HttpSession session, Model model) {
        var user = getUser(session);
        task.setUser(user);
        model.addAttribute(user);
        String[] array = req.getParameterValues("category.id");
        if (store.updateTask(task, array)) {
            return "redirect:/tasks";
        }
        model.addAttribute("message", "Ошибка при обновлении задачи.");
        return "task/404";
    }

    @PostMapping("/tasks/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model, HttpSession session) {
        if (store.deleteTask(id)) {
            return "redirect:/tasks";
        }
        model.addAttribute("user", getUser(session));
        model.addAttribute("message", "Ошибка при удалении задачи.");
        return "task/404";
    }

    @PostMapping("/tasks/completed/{id}")
    public String completedTask(Model model, @ModelAttribute Task task, HttpSession session) {
        if (store.completedTask(task)) {
            return "redirect:/tasks";
        }
        model.addAttribute("user", getUser(session));
        model.addAttribute("message", "Ошибка при завершении задачи.");
        return "task/404";
    }

}
