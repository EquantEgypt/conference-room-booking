UPDATE meeting_rooms
SET room_type = CASE WHEN capacity >= 25 THEN 'VIP' ELSE 'REGULAR' END
WHERE room_type IS NULL
   OR room_type NOT IN ('VIP', 'REGULAR');

