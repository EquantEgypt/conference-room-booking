-- Add missing columns to reservations table
ALTER TABLE reservations
    ADD COLUMN number_of_occurrences BIGINT;
ALTER TABLE reservations
    ADD COLUMN parent_reservation_id BIGINT;

-- Add foreign key constraint for parent_reservation_id (self-referencing)
ALTER TABLE reservations
    ADD CONSTRAINT fk_reservation_parent FOREIGN KEY (parent_reservation_id) REFERENCES reservations (reservation_id) ON DELETE CASCADE;
