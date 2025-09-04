-- V3: Properly convert VARCHAR columns to ENUM types
-- This migration ensures all enum columns are correctly converted

-- First, let's handle meeting_rooms table
ALTER TABLE meeting_rooms
MODIFY COLUMN room_type ENUM('VIP', 'NORMAL') NOT NULL;

ALTER TABLE meeting_rooms
MODIFY COLUMN status ENUM('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED') NOT NULL DEFAULT 'AVAILABLE';

-- Now handle reservations table
ALTER TABLE reservations
MODIFY COLUMN reservation_type ENUM('EXTERNAL', 'INTERNAL') NOT NULL;

ALTER TABLE reservations
MODIFY COLUMN recurrence_option ENUM('ONE_TIME', 'DAILY', 'WEEKLY', 'MONTHLY') NOT NULL DEFAULT 'ONE_TIME';

-- Finally handle users table
ALTER TABLE users
MODIFY COLUMN role ENUM('EMPLOYEE', 'MANAGER') NOT NULL;
