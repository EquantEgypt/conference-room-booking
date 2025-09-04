-- V4: Final fix for enum columns with direct conversion
-- This migration handles the case where existing data needs to be preserved during conversion

-- First, temporarily disable foreign key checks to avoid issues
SET foreign_key_checks = 0;

-- Convert users table role column
-- Update any existing data to uppercase values first
UPDATE users SET role = UPPER(role) WHERE role IS NOT NULL;

-- Now convert the column to ENUM
ALTER TABLE users
MODIFY COLUMN role ENUM('EMPLOYEE', 'MANAGER') NOT NULL;

-- Convert meeting_rooms table columns
-- Update existing data to ensure it matches enum values
UPDATE meeting_rooms SET room_type = UPPER(room_type) WHERE room_type IS NOT NULL;
UPDATE meeting_rooms SET status = UPPER(status) WHERE status IS NOT NULL;

ALTER TABLE meeting_rooms
MODIFY COLUMN room_type ENUM('VIP', 'NORMAL') NOT NULL;

ALTER TABLE meeting_rooms
MODIFY COLUMN status ENUM('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE';

-- Convert reservations table columns
-- Update existing data to ensure it matches enum values
UPDATE reservations SET reservation_type = UPPER(reservation_type) WHERE reservation_type IS NOT NULL;
UPDATE reservations SET recurrence_option = UPPER(recurrence_option) WHERE recurrence_option IS NOT NULL;

ALTER TABLE reservations
MODIFY COLUMN reservation_type ENUM('EXTERNAL', 'INTERNAL') NOT NULL;

ALTER TABLE reservations
MODIFY COLUMN recurrence_option ENUM('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY') NOT NULL DEFAULT 'ONE_TIME';

-- Re-enable foreign key checks
SET foreign_key_checks = 1;
