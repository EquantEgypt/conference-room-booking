CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role VARCHAR(50) NOT NULL,
    department_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_user_job FOREIGN KEY (job_id) REFERENCES jobs(id)
);
