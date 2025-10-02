-- Add missing columns and modify columns in reservations table
-- Change description to larger VARCHAR
ALTER TABLE reservations ALTER COLUMN description SET DATA TYPE VARCHAR(2000);
-- Change recurrence_end_date to DATE
ALTER TABLE reservations ALTER COLUMN recurrence_end_date SET DATA TYPE DATE;
-- Add title column
ALTER TABLE reservations ADD COLUMN title VARCHAR(50) NOT NULL DEFAULT '';
-- Drop old start_time and end_time (if present)
ALTER TABLE reservations DROP COLUMN IF EXISTS start_time;
ALTER TABLE reservations DROP COLUMN IF EXISTS end_time;
-- Add date and new start_time/end_time columns
ALTER TABLE reservations ADD COLUMN date DATE NOT NULL DEFAULT CURRENT_DATE;
ALTER TABLE reservations ADD COLUMN start_time TIME NOT NULL DEFAULT '00:00:00';
ALTER TABLE reservations ADD COLUMN end_time TIME NOT NULL DEFAULT '00:00:00';
