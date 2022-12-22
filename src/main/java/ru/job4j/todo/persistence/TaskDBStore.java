package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Task updateTask(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Task SET name = :fName, description = :fDescription, done = :fDone WHERE id = :fId")
                    .setParameter("fName", task.getName())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId()).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            log.error("Error HQL in updateTask.", e);
        }
        session.close();
        return task;
    }

    public Task completedTask(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "UPDATE Task SET done = :fDone WHERE id = :fId")
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Error HQL in completedTask.", e);
           session.getTransaction().rollback();
        }
        session.close();
        return task;
    }

    public List<Task> findAllOrderById() {
        var session = sf.openSession();
        List<Task> list = new ArrayList<>();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task ORDER BY id", Task.class).list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error HQL in findAllOrderById.", e);
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
            task = session.createQuery("FROM Task WHERE id = :fId", Task.class)
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
            session.createQuery("DELETE FROM Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error HQL in deleteTask.", e);
        }
    }

    /**
     * Метод выполняет поиск и возврат списка задач по флагу
     * @return список задач, сортировкой по id
     */
    public List<Task> findAtFlagAndOrderById(boolean flag) {
        var session = sf.openSession();
        List<Task> list = new ArrayList<>();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task WHERE done = :fDone ORDER BY id", Task.class)
                    .setParameter("fDone", flag)
                    .list();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            log.error("Error in findAtFlagAndOrderById.", e);
            session.getTransaction().rollback();
        }
        session.close();
        return list;
    }
}
