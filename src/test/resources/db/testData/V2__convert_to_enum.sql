-- H2-compatible conversion: normalize values and enforce CHECK constraints

-- Normalize existing values
UPDATE users
SET role = UPPER(TRIM(role))
WHERE role IS NOT NULL;
UPDATE meeting_rooms
SET room_type = UPPER(TRIM(room_type)),
    status    = UPPER(TRIM(status))
WHERE room_id IS NOT NULL;
UPDATE reservations
SET reservation_type  = UPPER(TRIM(reservation_type)),
    recurrence_option = UPPER(TRIM(recurrence_option))
WHERE reservation_id IS NOT NULL;

-- Change column types to VARCHAR in H2 and add CHECK constraints (H2 has no ENUM)
ALTER TABLE users
    ALTER COLUMN role SET DATA TYPE VARCHAR(50);
ALTER TABLE meeting_rooms
    ALTER COLUMN room_type SET DATA TYPE VARCHAR(20);
ALTER TABLE meeting_rooms
    ALTER COLUMN status SET DATA TYPE VARCHAR(20);
ALTER TABLE reservations
    ALTER COLUMN reservation_type SET DATA TYPE VARCHAR(20);
ALTER TABLE reservations
    ALTER COLUMN recurrence_option SET DATA TYPE VARCHAR(20);

-- Add CHECK constraints to mimic ENUM semantics
ALTER TABLE users
    ADD CONSTRAINT chk_users_role CHECK (role IN ('EMPLOYEE', 'MANAGER'));
ALTER TABLE meeting_rooms
    ADD CONSTRAINT chk_meeting_rooms_room_type CHECK (room_type IN ('VIP', 'REGULAR'));
ALTER TABLE meeting_rooms
    ADD CONSTRAINT chk_meeting_rooms_status CHECK (status IN ('AVAILABLE', 'UNDER_MAINTENANCE', 'BOOKED'));
ALTER TABLE reservations
    ADD CONSTRAINT chk_reservations_reservation_type CHECK (reservation_type IN ('EXTERNAL', 'INTERNAL'));
ALTER TABLE reservations
    ADD CONSTRAINT chk_reservations_recurrence_option CHECK (recurrence_option IN ('ONE_TIME', 'DAILY', 'WEEKLY'));
