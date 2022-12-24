package ru.job4j.todo.persistence;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Command -  это поведенческий шаблон программирования.
 * Цель данного шаблона - инкапсулировать запрос в объект.
 * Это нужно для того, чтобы отделить методы от их конкретной реализации.
 * Cоздать общее поведение, в котором будет выполняться абстрактная команда,
 * а реализация этой команды будет вынесена отдельно, при этом получается возможность
 * менять реализацию, не меняя код самого метода (как в паттерне Стратегия).
 * <p>
 * Превращать запросы в объекты полезно, например, для передачи этих запросов в виде
 * параметров методов, постановки их в очередь, логирования или создания поддержки отмены
 * операций. В нашем случае за счет применения этого шаблона сокращается кол-во написанного кода.
 */

@Repository
@AllArgsConstructor
public class CrudRepository {
    private final SessionFactory sf;

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sq = session
                    .createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return Optional.ofNullable(sq.getSingleResult());
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sq = session
                    .createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.list();
        };
        return tx(command);
    }

    /**
     * @param command - это команда, которую непосредственно нужно выполнить.
     *                Команда записывается в виде лямбды, чтобы сделать код более компактным.
     *                В этом же методе вызывается метод tx(), который выполняет команды,
     *                и передаётся ему получившуюся команда command.
     */
    public <T> T tx(Function<Session, T> command) {
        var session = sf.openSession();
        try (session) {
            var tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            var tx = session.getTransaction();
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
