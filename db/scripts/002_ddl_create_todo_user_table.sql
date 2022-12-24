CREATE TABLE IF NOT EXISTS todo_user
(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    login VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);

COMMENT ON TABLE todo_user is 'Таблица пользователей'