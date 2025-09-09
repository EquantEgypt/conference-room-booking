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


INSERT IGNORE INTO users (role, first_name, last_name, phone,  business_email, password, department_id, job_id) VALUES
('EMPLOYEE', 'Seif', 'Ehab', '01002756766', 'seif.ehab@orange.com', '$2a$12$9R1xTuq1LAuRJwPXqxtQYOCGMlEXPEXnNhPRH3vsR6mgdHzXw3/z.',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'Software Developer')
),
('EMPLOYEE', 'Fatma', 'Hesham', '01002756767', 'fatma.hesham@orange.com', '$2y$10$.Jitaf99rf4bbGvswDlnkOA9MP2kQkaROQk7DkDxXy7FGYWgRzip2',
    (SELECT department_id FROM departments WHERE name = 'Quality Assurance'),
    (SELECT job_id FROM jobs WHERE title = 'QA Engineer')
),

 ('MANAGER', 'Marim', 'Mohamed', '01042756769', 'marim.mohamed@orange.com', '$2y$10$1PeW.8pRWQ.5J4xIPPfdWOw55DmuDGB2BhdB2lgKTQCNmfk/Tcxzi',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'Team Lead')
),
('EMPLOYEE', 'Nadine', 'Eid', '01052756770', 'nadine.eid@orange.com', '$2y$10$CjE4S03fQS1ejediWNvhJuRbGZYXeFwQfS/7Pk2Vc77FKwznuPg1S',
    (SELECT department_id FROM departments WHERE name = 'Product'),
    (SELECT job_id FROM jobs WHERE title = 'Product Manager')
),
('EMPLOYEE', 'Omar', 'Ahmed', '01002756771', 'omar.ahmed@orange.com', '$2y$10$SWG/2l/Gv/Sf.yvj/thDdO.vtehr3UhwyKHrvN97vP7x6HjawlM2e',
    (SELECT department_id FROM departments WHERE name = 'Software'),
    (SELECT job_id FROM jobs WHERE title = 'DevOps Engineer')
);



INSERT IGNORE INTO meeting_rooms (name, capacity, building, floor, geo_location, operating_hours_start, operating_hours_end, room_type, status) VALUES
('Nefertiti', 30, 'Nasr City Branch', 4, '30.0561,31.3300', '09:00', '18:00', 'VIP', 'AVAILABLE'),
('Ramses', 25, 'Alexandria Office', 1, '31.2001,29.9187', '09:00', '18:00', 'REGULAR', 'AVAILABLE'),
('Tutankhamun', 15, 'Galleria 40 - Sheikh Zayed', 5, '30.0187,31.0011', '09:00', '18:00', 'REGULAR', 'UNDER_MAINTENANCE'),
('Cleopatra', 11, 'Nasr City Branch', 1, '30.0561,31.3300', '09:00', '18:00', 'REGULAR', 'AVAILABLE'),
('Karnak', 21, 'Downtown Cairo HQ', 2, '30.0444,31.2357', '09:00', '18:00', 'VIP', 'AVAILABLE'),
('Luxor', 4, 'Downtown Cairo HQ', 4, '30.0444,31.2357', '09:00', '18:00', 'REGULAR', 'UNDER_MAINTENANCE'),
('Memphis', 17, 'Downtown Cairo HQ', 1, '30.0444,31.2357','09:00', '18:00', 'REGULAR', 'UNDER_MAINTENANCE'),
('Le Nil', 29, 'Smart Village - Cairo', 2, '30.0725,31.0135', '09:00', '18:00', 'VIP', 'AVAILABLE'),
('Sphinx', 27, 'Downtown Cairo HQ', 2, '30.0444,31.2357','09:00', '18:00', 'VIP', 'AVAILABLE'),
('Philae', 18, 'Galleria 40 - Sheikh Zayed', 3, '30.0187,31.0011','09:00', '18:00', 'REGULAR', 'AVAILABLE');


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

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN (
    'Projector','Whiteboard','Video Conferencing Unit','Conference Phone','Speaker System',
    'Microphone','LED Screen','HDMI Cables','Wi-Fi Access Point','Smart Board',
    'Printer','Scanner','Air Conditioner','Lighting Control','Podium'
)
WHERE mr.name = 'Nefertiti';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Projector','Whiteboard','Conference Phone','Wi-Fi Access Point','Air Conditioner')
WHERE mr.name = 'Ramses';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Projector','Video Conferencing Unit','Speaker System','LED Screen','HDMI Cables','Smart Board')
WHERE mr.name = 'Tutankhamun';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Conference Phone','Microphone','Printer','Scanner','Lighting Control')
WHERE mr.name = 'Cleopatra';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN (
    'Projector','Whiteboard','Video Conferencing Unit','Conference Phone','Speaker System',
    'Microphone','LED Screen','HDMI Cables','Wi-Fi Access Point','Smart Board',
    'Printer','Scanner','Air Conditioner','Lighting Control','Podium'
)
WHERE mr.name = 'Karnak';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Projector','Whiteboard','Video Conferencing Unit','Speaker System','HDMI Cables','Air Conditioner')
WHERE mr.name = 'Luxor';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Smart Board','Printer','Scanner','Podium','Wi-Fi Access Point')
WHERE mr.name = 'Memphis';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN (
    'Projector','Whiteboard','Video Conferencing Unit','Conference Phone','Speaker System',
    'Microphone','LED Screen','HDMI Cables','Wi-Fi Access Point','Smart Board',
    'Printer','Scanner','Air Conditioner','Lighting Control','Podium'
)
WHERE mr.name = 'Le Nil';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN (
    'Projector','Whiteboard','Video Conferencing Unit','Conference Phone','Speaker System',
    'Microphone','LED Screen','HDMI Cables','Wi-Fi Access Point','Smart Board',
    'Printer','Scanner','Air Conditioner','Lighting Control','Podium'
)
WHERE mr.name = 'Sphinx';

INSERT INTO meetingroom_equipment (meetingroom_id, equipment_id)
SELECT mr.room_id, e.equipment_id
FROM meeting_rooms mr
JOIN equipment e ON e.type IN ('Projector','Microphone','LED Screen','Conference Phone')
WHERE mr.name = 'Philae';


INSERT IGNORE INTO reservations (description, start_time, end_time, reservation_type, recurrence_option, user_id, room_id) VALUES
('Sprint Planning', '2025-09-05 09:00:00', '2025-09-05 10:00:00', 'INTERNAL', 'WEEKLY',
    (SELECT user_id FROM users WHERE  business_email = 'seif.ehab@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Sphinx')
),
('Project Review', '2025-09-06 14:00:00', '2025-09-06 15:30:00', 'EXTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE  business_email ='fatma.hesham@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Philae')
),
('Weekly Standup', '2025-09-07 10:00:00', '2025-09-07 10:30:00', 'INTERNAL', 'WEEKLY',
    (SELECT user_id FROM users WHERE  business_email = 'nadine.eid@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Cleopatra')
),
('HR Meeting', '2025-09-08 11:00:00', '2025-09-08 12:00:00', 'INTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE  business_email = 'marim.mohamed@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Nefertiti')
),
('Client Demo', '2025-09-09 15:00:00', '2025-09-09 16:00:00', 'EXTERNAL', 'ONE_TIME',
    (SELECT user_id FROM users WHERE  business_email = 'omar.ahmed@orange.com'),
    (SELECT room_id FROM meeting_rooms WHERE name = 'Nefertiti')
);








SET foreign_key_checks = 1;
