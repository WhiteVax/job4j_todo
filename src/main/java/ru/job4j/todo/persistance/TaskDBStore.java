package ru.job4j.todo.persistance;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.time.LocalDate;
import java.util.*;

/**
 * Хранилище данных задач
 */
@Repository
@AllArgsConstructor
@Slf4j
public class TaskDBStore {
    private final SessionFactory sf;

    public Task createTask(Task task) {
        var session = sf.openSession();
        session.beginTransaction();
        task.setCreated(LocalDate.now());
        session.saveOrUpdate(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public Task saveOrUpdate(Task task) {
        var session = sf.openSession();
        session.beginTransaction();
        session.saveOrUpdate(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public List<Task> findAllOrderById() {
        var session = sf.openSession();
        List<Task> list = new ArrayList<>();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task t ORDER BY t.id", Task.class).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error in findAllOrderById.", e);
            session.getTransaction().rollback();
        }
        session.close();
        return list;
    }

    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        Optional<Task> task = Optional.empty();
        try {
            session.beginTransaction();
            task = session.createQuery("FROM Task t WHERE t.id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error HQL in findById.", e);
            session.getTransaction().rollback();
        }
        session.close();
        return task;
    }

    public void deleteTask(int id) {
        try {
            var session = sf.openSession();
            session.beginTransaction();
            session.createQuery("DELETE FROM Task t WHERE t.id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error in deleteTask.", e);
        }
    }

    /**
     * Метод выполняет поиск и возврат списка задач, отсортированный по id
     * @return список выполненных задач
     */
    public List<Task> findOnlyDoneTaskOrderById() {
        var session = sf.openSession();
        List<Task> list = new ArrayList<>();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task t WHERE t.done = true ORDER BY t.id", Task.class).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error in findOnlyDoneTaskOrderById.", e);
            session.getTransaction().rollback();
        }
        session.close();
        return list;
    }

    /**
     * Метод выполняет поиск и возврат списка задач, отсортированный по дате создания
     * @return список новых задач
     */
    public List<Task> findOnlyNewTaskOrderByCreated() {
        var session = sf.openSession();
        List<Task> list = new ArrayList<>();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task t WHERE t.done = false ORDER BY t.created", Task.class).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error in findOnlyNewTaskOrderByCreated.", e);
            session.getTransaction().rollback();
        }
        session.close();
        return list;
    }
}
