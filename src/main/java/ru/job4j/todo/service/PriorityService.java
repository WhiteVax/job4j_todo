package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.persistence.PriorityDBStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PriorityService {
    private PriorityDBStore store;

    public List<Priority> getAll() {
        return store.findAllOrderByPosition();
    }

    public Optional<Priority> findById(int id) {
        return store.findById(id);
    }
 }
