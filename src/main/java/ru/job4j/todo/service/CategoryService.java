package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.persistence.CategoryDBStory;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private CategoryDBStory categoryDBStory;

    public List<Category> getAll() {
        return categoryDBStory.getAllOrderById();
    }
}
