-- Add missing columns to reservations table
ALTER TABLE reservations
ADD COLUMN repeat_count BIGINT,
ADD COLUMN number_of_occurrences BIGINT,
ADD COLUMN parent_id BIGINT;

-- Add foreign key constraint for parent_id (self-referencing)
ALTER TABLE reservations
ADD CONSTRAINT fk_reservation_parent
FOREIGN KEY (parent_id) REFERENCES reservations(reservation_id) ON DELETE CASCADE;
