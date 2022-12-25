ALTER TABLE tasks ADD COLUMN user_id INT;

ALTER TABLE tasks ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES todo_user(id);

COMMENT ON COLUMN tasks.user_id IS 'Поле пользователя который добавил задание'