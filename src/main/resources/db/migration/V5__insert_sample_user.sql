-- V5: Insert sample user with software department and junior developer job

-- Insert Software department (ignore if already exists)
INSERT IGNORE INTO departments (dept_name) VALUES ('Software');

-- Insert Junior Software Developer job (ignore if already exists)
INSERT IGNORE INTO jobs (title) VALUES ('Junior Software Developer');

-- Insert sample user with bcrypted password (ignore if already exists)
-- Password is "password123" bcrypted with strength 12
INSERT IGNORE INTO users (
    role,
    first_name,
    last_name,
    phone,
    business_email,
    password,
    department_id,
    job_id
) VALUES (
    'EMPLOYEE',
    'Seif',
    'Ehab',
    '+1234567890',
    'semaziz2004@yahoo.com',
    '$2y$12$IcqyjC86VbvAUbeKk1QpEuyafXHTH0dhBOts.topLuRlrLIG40.sS',
    (SELECT dept_id FROM departments WHERE dept_name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'Junior Software Developer')
);
