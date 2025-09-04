SET foreign_key_checks = 0;

INSERT IGNORE INTO jobs (title) VALUES
            ('Software Developer'),
            ('QA Engineer'),
            ('DevOps Engineer'),
            ('HR Specialist'),
            ('IT Support'),
            ('Team Lead'),
            ('Product Manager');


INSERT IGNORE INTO departments (name) VALUES
('Software'),
('Quality Assurance'),
('Human Resources'),
('Product');


INSERT IGNORE INTO users (role, first_name, last_name, phone, business_email, password, department_id, job_id) VALUES
('EMPLOYEE', 'Seif', 'Ehab', '01002756766', 'seif.ehab@orange.com', '$2y$10$zH3b1gAG/hOPSJn2HcYIiuWltjzwtY8.q3DyELOHF19xWy7sJAoTS',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'Software Developer')
),
('EMPLOYEE', 'Mona', 'Ali', '01002756767', 'mona.ali@orange.com', '$2y$10$abc123hashedpassword',
    (SELECT department_id FROM departments WHERE name = 'Quality Assurance'),
    (SELECT job_id FROM jobs WHERE title = 'QA Engineer')
),

('EMPLOYEE', 'Sara', 'Omar', '01042756769', 'sara.omar@orange.com', '$2y$10$ghi789hashedpassword',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'Team Lead')
),
('MANAGER', 'Laila', 'Mohamed', '01052756770', 'laila.mohamed@orange.com', '$2y$10$lmn456hashedpassword',
    (SELECT department_id FROM departments WHERE name = 'Product'),
    (SELECT job_id FROM jobs WHERE title = 'Product Manager')
),
('EMPLOYEE', 'Ahmed', 'Tamer', '01002756771', 'ahmed.tamer@orange.com', '$2y$10$opq789hashedpassword',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'DevOps Engineer')
);

INSERT IGNORE INTO meeting_rooms (name, capacity, building, floor, geo_location, equipment, operating_hours_start, operating_hours_end, room_type, status) VALUES
('Cleopatra', 10, 'Smart Village - Cairo', 4,'31.2001,29.9187', 'Projector, Whiteboard', '09:00', '18:00', 'NORMAL', 'AVAILABLE'),
('Philae', 35, 'Galleria 40 - Sheikh Zayed', 2,'30.0187,31.0011', 'LED Screen, Microphone, Projector, Scanner', '09:00', '18:00', 'VIP', 'AVAILABLE'),
('Sphinx', 15, 'Galleria 40 - Sheikh Zayed', 3,'36.8144,30.0444', 'Smart Board, HDMI Cables' , '09:00', '18:00', 'NORMAL', 'UNDER_MAINTENANCE'),
('Nefertiti', 25, 'Nasr City Branch', 3 , '36.8143,10.1751', 'Smart Board, Conference Phone', '09:00', '18:00', 'NORMAL', 'BOOKED');


INSERT IGNORE INTO reservations (description, start_time, end_time, reservation_type, recurrence_option, user_id, room_id) VALUES
('Sprint Planning', '2025-09-05 09:00:00', '2025-09-05 10:00:00', 'INTERNAL', 'WEEKLY',
    (SELECT user_id FROM users WHERE business_email = 'seif.ehab@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Sphinx')
),
('Project Review', '2025-09-06 14:00:00', '2025-09-06 15:30:00', 'EXTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE business_email = 'mona.ali@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Philae')
),
('Weekly Standup', '2025-09-07 10:00:00', '2025-09-07 10:30:00', 'INTERNAL', 'WEEKLY',
    (SELECT user_id FROM users WHERE business_email = 'laila.mohamed@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Cleopatra')
),
('HR Meeting', '2025-09-08 11:00:00', '2025-09-08 12:00:00', 'INTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE business_email = 'sara.omar@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Nefertiti')
),
('Client Demo', '2025-09-09 15:00:00', '2025-09-09 16:00:00', 'EXTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE business_email = 'ahmed.tamer@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Nefertiti')
);

SET foreign_key_checks = 1;
