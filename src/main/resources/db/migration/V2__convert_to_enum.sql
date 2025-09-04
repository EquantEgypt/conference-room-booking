SET foreign_key_checks = 0;

UPDATE users SET role = UPPER(TRIM(role)) WHERE role IS NOT NULL;
UPDATE meeting_rooms SET room_type = UPPER(TRIM(room_type)), status = UPPER(TRIM(status)) WHERE id IS NOT NULL;
UPDATE reservations SET reservation_type = UPPER(TRIM(reservation_type)), recurrence_option = UPPER(TRIM(recurrence_option)) WHERE id IS NOT NULL;


ALTER TABLE users
MODIFY COLUMN role ENUM('EMPLOYEE', 'MANAGER') NOT NULL DEFAULT 'EMPLOYEE';

ALTER TABLE meeting_rooms
MODIFY COLUMN room_type ENUM('VIP', 'NORMAL') NOT NULL,
MODIFY COLUMN status ENUM('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE';

ALTER TABLE reservations
MODIFY COLUMN reservation_type ENUM('EXTERNAL', 'INTERNAL') NOT NULL,
MODIFY COLUMN recurrence_option ENUM('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY') NOT NULL DEFAULT 'ONE_TIME';

SET foreign_key_checks = 1;
