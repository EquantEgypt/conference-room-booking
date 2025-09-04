CREATE TABLE jobs (
    job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE departments (
    department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    manager_id BIGINT
);


CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    business_email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    department_id BIGINT NOT NULL,
    job_id BIGINT NOT NULL,
    CONSTRAINT fk_user_department FOREIGN KEY (department_id) REFERENCES departments(department_id),
    CONSTRAINT fk_user_job FOREIGN KEY (job_id) REFERENCES jobs(job_id)
);

ALTER TABLE departments ADD CONSTRAINT fk_department_manager FOREIGN KEY (manager_id) REFERENCES users(user_id);


CREATE TABLE equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE meeting_rooms (
    room_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    capacity INT NOT NULL,
    building VARCHAR(100) NOT NULL,
    floor INT NOT NULL,
    phone VARCHAR(50) UNIQUE,
    geo_location VARCHAR(255),
    equipment TEXT,
    operating_hours_start TIME NOT NULL,
    operating_hours_end TIME NOT NULL,
    room_type VARCHAR(20) NOT NULL CHECK (room_type IN ('VIP', 'NORMAL')),
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
        CHECK (status IN ('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED'))
);

CREATE TABLE reservations (
    reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    reservation_type VARCHAR(20) NOT NULL,
    recurrence_option VARCHAR(20) NOT NULL DEFAULT 'ONE_TIME'
            CHECK (recurrence_option IN ('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY')),

    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,

    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES meeting_rooms(room_id)
);

CREATE TABLE meetingroom_equipment (
    meetingroom_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    PRIMARY KEY (meetingroom_id, equipment_id),
    CONSTRAINT fk_meetingroom_equipment_room FOREIGN KEY (meetingroom_id) REFERENCES meeting_rooms(room_id),
    CONSTRAINT fk_meetingroom_equipment_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id)
);
