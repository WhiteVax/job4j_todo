package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.persistance.TaskDBStore;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TaskService {
    private final TaskDBStore store;

    public Task createTask(Task task) {
        return store.createTask(task);
    }

    public Task updateTask(Task task) {
        return store.saveOrUpdate(task);
    }

    public List<Task> findAllTask() {
        return store.findAllOrderById();
    }

    public List<Task> findNewTask() {
        return store.findOnlyNewTaskOrderByCreated();
    }

    public List<Task> findOldTask() {
        return store.findOnlyDoneTaskOrderById();
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public void deleteTask(int id) {
        store.deleteTask(id);
    }
}
