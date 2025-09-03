
CREATE TABLE meeting_rooms (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

