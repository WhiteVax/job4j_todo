package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

@Controller
public class TaskController {

    private final TaskService store;

    public TaskController(TaskService store) {
        this.store = store;
    }

    @GetMapping("")
    public String main() {
        return "task/index";
    }

    @GetMapping("/tasks")
    public String allTasks(Model model) {
        model.addAttribute("tasks", store.findAllTask());
        return "task/tasks";
    }

    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("tasks", store.findNewTask());
        return "task/new";
    }

    @GetMapping("/done")
    public String doneTask(Model model) {
        model.addAttribute("tasks", store.findOldTask());
        return "task/done";
    }

    @GetMapping("/tasks/{id}")
    public String getTask(Model model, @PathVariable("id") int id) {
        var task = store.findById(id).get();
        model.addAttribute("task", task);
        return "task/task";
    }

    @GetMapping("/tasks/add")
    public String createTask(Model model) {
        model.addAttribute("task", model);
        return "formAdd";
    }

    @PostMapping("/create")
    public String addTask(@ModelAttribute Task task) {
        store.createTask(task);
        return "redirect:/tasks";
    }

    @GetMapping("/tasks/update/{id}")
    public String formUpdateTask(Model model, @PathVariable("id") int id) {
        var task = store.findById(id).get();
        model.addAttribute("task", task);
        return "formUpdate";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task) {
        store.updateTask(task);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        store.deleteTask(id);
        return "redirect:/tasks";
    }

    @PostMapping("/tasks/completed/{id}")
    public String completedTask(@ModelAttribute Task task) {
        task.setDone(true);
        store.completedTask(task);
        return "redirect:/tasks";
    }
}
