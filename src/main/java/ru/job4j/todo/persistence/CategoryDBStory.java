package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Хранилище категорий к задачам
 */
@Repository
@AllArgsConstructor
public class CategoryDBStory {
    private final CrudRepository crudRepository;

    public List<Category> getAllOrderById() {
        return crudRepository.query("FROM Category ORDER BY id", Category.class);
    }

    public Optional<Category> getCategoryById(int id) {
        return crudRepository.optional("FROM Category WHERE id = :fId", Category.class,
                Map.of("fId", id));
    }
}
