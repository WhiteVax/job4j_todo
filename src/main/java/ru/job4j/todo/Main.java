package ru.job4j.todo;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    /**
     * SessionFactory конфигуратор, используеться для инициализации пула,
     * метод configure() читает файл hibernate.cfg.xm со всеми настройками.
     * Создаётся через фабрику один раз для всего приложения.
     * @return Session объект для манипуляци с БД.
     */
    @Bean(destroyMethod = "close")
    public SessionFactory sf() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
