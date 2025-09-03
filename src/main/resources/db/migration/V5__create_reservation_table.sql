CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    reservation_type VARCHAR(20) NOT NULL,
    recurrence_option VARCHAR(20) NOT NULL DEFAULT 'ONE_TIME'
            CHECK (recurrence_pattern IN ('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY')),

    user_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,

    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reservation_room FOREIGN KEY (room_id) REFERENCES meeting_rooms(id)
);


