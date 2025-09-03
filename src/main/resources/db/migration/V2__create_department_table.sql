CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    manager_id BIGINT,
    CONSTRAINT fk_department_manager FOREIGN KEY (manager_id) REFERENCES users(id)
);