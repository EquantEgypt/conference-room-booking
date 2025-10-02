-- Add missing columns and modify columns in reservations table
ALTER TABLE reservations
    MODIFY COLUMN description TEXT,
    MODIFY COLUMN recurrence_end_date DATE,
    ADD COLUMN title VARCHAR (50) NOT NULL,
DROP
COLUMN start_time,
    DROP
COLUMN end_time,
    ADD COLUMN date DATE NOT NULL,
    ADD COLUMN start_time TIME NOT NULL,
    ADD COLUMN end_time TIME NOT NULL;

