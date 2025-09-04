-- Fix room_type and status columns to use ENUM instead of VARCHAR
ALTER TABLE meeting_rooms
MODIFY COLUMN room_type ENUM('VIP', 'NORMAL') NOT NULL;

ALTER TABLE meeting_rooms
MODIFY COLUMN status ENUM('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE';

-- Fix reservation_type and recurrence_option columns to use ENUM instead of VARCHAR
ALTER TABLE reservations
MODIFY COLUMN reservation_type ENUM('EXTERNAL', 'INTERNAL') NOT NULL;

ALTER TABLE reservations
MODIFY COLUMN recurrence_option ENUM('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY') NOT NULL DEFAULT 'ONE_TIME';
