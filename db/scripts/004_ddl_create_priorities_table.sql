CREATE TABLE IF NOT EXISTS priorities
(
    id       SERIAL PRIMARY KEY,
    name     TEXT UNIQUE NOT NULL,
    position INT
);

COMMENT ON TABLE priorities IS 'Приоритеты задач'