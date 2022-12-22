CREATE TABLE IF NOT EXISTS todo_user
(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    login VARCHAR,
    password VARCHAR
);

COMMENT ON TABLE todo_user is 'Таблица пользователей'