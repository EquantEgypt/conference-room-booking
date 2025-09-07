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


INSERT IGNORE INTO equipment (type) VALUES
('Projector'),
('Whiteboard'),
('Video Conferencing Unit'),
('Conference Phone'),
('Speaker System'),
('Microphone'),
('LED Screen'),
('HDMI Cables'),
('Wi-Fi Access Point'),
('Smart Board'),
('Printer'),
('Scanner'),
('Air Conditioner'),
('Lighting Control'),
('Podium');


INSERT IGNORE INTO users (role, first_name, last_name, phone, business_email, password, department_id, job_id) VALUES
('EMPLOYEE', 'Seif', 'Ehab', '01002756766', 'seif.ehab@orange.com', '$2a$12$9R1xTuq1LAuRJwPXqxtQYOCGMlEXPEXnNhPRH3vsR6mgdHzXw3/z.',
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
('Nefertiti', 30, 'Nasr City Branch', 4, '30.0561,31.3300', 'Podium, Video Conferencing Unit, Microphone, Wi-Fi Access Point, Air Conditioner, Whiteboard, Speaker System, Printer, LED Screen, Projector, HDMI Cables, Lighting Control', '09:00', '18:00', 'VIP', 'AVAILABLE'),
('Ramses', 25, 'Alexandria Office', 1, '31.2001,29.9187', 'Scanner, Conference Phone, Whiteboard, Video Conferencing Unit, HDMI Cables, Printer, Speaker System, Lighting Control', '09:00', '18:00', 'NORMAL', 'UNDER_MAINTENANCE'),
('Tutankhamun', 9, 'Galleria 40 - Sheikh Zayed', 5, '30.0187,31.0011', 'LED Screen, Scanner, Conference Phone, Video Conferencing Unit, Wi-Fi Access Point, HDMI Cables, Podium, Whiteboard, Speaker System, Smart Board, Printer, Projector, Microphone, Lighting Control, Air Conditioner', '09:00', '18:00', 'NORMAL', 'UNDER_MAINTENANCE'),
('Cleopatra', 11, 'Nasr City Branch', 1, '30.0561,31.3300', 'HDMI Cables, Printer, LED Screen, Microphone, Wi-Fi Access Point, Scanner, Projector', '09:00', '18:00', 'NORMAL', 'AVAILABLE'),
('Karnak', 21, 'Downtown Cairo HQ', 2, '30.0444,31.2357', 'Air Conditioner, Projector, Printer, HDMI Cables, Speaker System, Microphone, Smart Board, Video Conferencing Unit, Lighting Control, Scanner', '09:00', '18:00', 'VIP', 'UNDER_MAINTENANCE'),
('Luxor', 4, 'Downtown Cairo HQ', 4, '30.0444,31.2357', 'Podium, Printer, Smart Board, Microphone, Whiteboard, LED Screen, Scanner, Air Conditioner, Projector, Video Conferencing Unit, Speaker System, Wi-Fi Access Point, Conference Phone', '09:00', '18:00', 'NORMAL', 'UNDER_MAINTENANCE'),
('Aswan', 17, 'Downtown Cairo HQ', 1, '30.0444,31.2357', 'Lighting Control, Microphone, Air Conditioner, LED Screen, Printer, Projector, Conference Phone, HDMI Cables, Wi-Fi Access Point', '09:00', '18:00', 'NORMAL', 'AVAILABLE'),
('Memphis', 29, 'Smart Village - Cairo', 2, '30.0725,31.0135', 'Lighting Control, Podium, Wi-Fi Access Point, Video Conferencing Unit, Printer, HDMI Cables, Smart Board, Speaker System, Conference Phone, Air Conditioner, Microphone, LED Screen, Whiteboard, Projector, Scanner', '09:00', '18:00', 'VIP', 'UNDER_MAINTENANCE'),
('Sphinx', 27, 'Downtown Cairo HQ', 2, '30.0444,31.2357', 'Microphone, Projector, Podium, Wi-Fi Access Point, Conference Phone, HDMI Cables, Video Conferencing Unit, LED Screen, Speaker System, Air Conditioner, Lighting Control, Scanner, Smart Board, Printer, Whiteboard', '09:00', '18:00', 'VIP', 'UNDER_MAINTENANCE'),
('Philae', 18, 'Galleria 40 - Sheikh Zayed', 3, '30.0187,31.0011', 'Scanner, Lighting Control, Printer, Video Conferencing Unit, Projector, Wi-Fi Access Point, Microphone, HDMI Cables, Smart Board, Podium', '09:00', '18:00', 'NORMAL', 'AVAILABLE');


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



INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.id
FROM meeting_rooms mr, equipment e
WHERE mr.name = 'Cleopatra' AND e.type IN ('Projector', 'Whiteboard');


INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.id
FROM meeting_rooms mr, equipment e
WHERE mr.name = 'Philae' AND e.type IN ('LED Screen', 'Microphone', 'Projector', 'Scanner');


INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.id
FROM meeting_rooms mr, equipment e
WHERE mr.name = 'Sphinx' AND e.type IN ('Smart Board', 'HDMI Cables');


INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.id
FROM meeting_rooms mr, equipment e
WHERE mr.name = 'Nefertiti' AND e.type IN ('Smart Board', 'Conference Phone');






SET foreign_key_checks = 1;
