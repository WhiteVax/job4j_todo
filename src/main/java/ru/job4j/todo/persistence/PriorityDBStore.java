package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Хранилище приоритета к задачи
 */
@Repository
@AllArgsConstructor
public class PriorityDBStore {
    private final CrudRepository crudRepository;

    public List<Priority> findAllOrderByPosition() {
        return crudRepository.query("FROM Priority ORDER BY position", Priority.class);
    }

    public Optional<Priority> findById(int id) {
        return crudRepository.optional("FROM Priority WHERE id = :fId", Priority.class,
                Map.of("fId", id));
    }
}
