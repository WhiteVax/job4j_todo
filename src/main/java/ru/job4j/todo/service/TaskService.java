package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.persistence.PriorityDBStore;
import ru.job4j.todo.persistence.TaskDBStore;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TaskService {
    private final TaskDBStore store;
    private final PriorityDBStore priorities;

    public boolean createTask(Task task) {
        boolean rsl = false;
        if (priorities.findById(task.getPriority().getId()).isPresent()) {
            store.createTask(task);
            rsl = true;
        }
        return rsl;
    }

    public boolean updateTask(Task task) {
        var tmp = store.findById(task.getId());
            store.updateTask(task);
        return tmp.isPresent();
    }

    public boolean completedTask(Task task) {
        var rsl = store.findById(task.getId());
        task.setDone(true);
        store.completedTask(task);
        return rsl.isPresent();
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

    public boolean deleteTask(int id) {
        var rsl = store.findById(id);
        store.deleteTask(id);
        return rsl.isPresent();
    }
}
