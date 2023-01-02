package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Хранилище данных задач
 */
@Repository
@AllArgsConstructor
@Slf4j
public class TaskDBStore {
    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе.
     * @param task
     * @return task с id.
     */
    public Task createTask(Task task) {
        task.setCreated(LocalDateTime.now());
        crudRepository.run(session ->
            session.persist("task", task));
        return task;
    }

    /**
     * Обновить в базе задачу (имя, описание).
     * @param task обновлённую.
     */
    public Task updateTask(Task task) {
        crudRepository.run(session -> session.merge(task));
        return task;
    }

    /**
     * Выполняет задачу
     * @param task на вход получает задачу с ид и флагом
     * @return возвращает обновлённую задачу
     */
    public Task completedTask(Task task) {
        crudRepository.run(
                "UPDATE Task SET done = :fDone WHERE id = :fId",
                Map.of("fDone", task.isDone(),
                        "fId", task.getId())
        );
        return task;
    }

    /**
     * Поиск всех задач
     * @return List<task> сортировка по ид
     */
    public List<Task> findAllOrderById() {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority ORDER BY t.id", Task.class);
    }

    /**
     * Поиск задачи
     * @param id для поиска
     * @return возвращает объект Optional который содержит задачу или пустую обёртку
     */
    public Optional<Task> findById(int id) {
        return crudRepository.optional("FROM Task t JOIN FETCH t.priority JOIN FETCH t.categories WHERE t.id = :fId ", Task.class,
                Map.of("fId", id));
    }

    /**
     * Удаляет задачу по id
     */
    public void deleteTask(int id) {
        crudRepository.run("DELETE FROM Task WHERE id = :fId", Map.of("fId", id));
    }

    /**
     * Метод выполняет поиск и возврат списка задач по флагу
     * @return список задач, сортировкой по id
     */
    public List<Task> findAtFlagAndOrderById(boolean flag) {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority WHERE t.done = :fDone ORDER BY t.id", Task.class,
                Map.of("fDone", flag));
    }
}
