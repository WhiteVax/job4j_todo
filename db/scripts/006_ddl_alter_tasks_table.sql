ALTER TABLE tasks ADD COLUMN priority_id int REFERENCES priorities(id);

COMMENT ON COLUMN tasks.priority_id IS 'Столбец для приоритета задач с внешней таблицы'