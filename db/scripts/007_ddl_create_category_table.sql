CREATE TABLE IF NOT EXISTS categories
(
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

COMMENT ON TABLE categories IS 'Таблица категорий задач'