package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.persistence.TaskDBStore;

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
        return store.updateTask(task);
    }

    public Task completedTask(Task task) {
        return store.completedTask(task);
    }

    public List<Task> findAllTask() {
        return store.findAllOrderById();
    }

    public List<Task> findNewTask() {
        return store.findAtFlagAndOrderById(false);
    }

    public List<Task> findOldTask() {
        return store.findAtFlagAndOrderById(true);
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public void deleteTask(int id) {
        store.deleteTask(id);
    }
}
