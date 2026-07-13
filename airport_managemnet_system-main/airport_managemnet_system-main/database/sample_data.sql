USE airport_management;

-- Demo users are not inserted here because passwords are stored using BCrypt hashes.
-- Run src/main/java/com/airport/util/UserSeeder.java to create demo users:
-- spiderman / spidey
-- mj / mj

INSERT INTO passengers VALUES
(DEFAULT, 'hello', 'hello@gmail.com', '+91-1234567890', 'IND1234567', 'Indian'),
(DEFAULT, 'world', 'world@example.com', '+91-5555555555', 'IND7654321', 'Indian');

INSERT INTO flights VALUES
(DEFAULT, 'AI101', 'Air India'),
(DEFAULT, '6E205', 'IndiGo');

INSERT INTO trips VALUES
(DEFAULT, 1, 'Delhi', 'Mumbai', '2026-06-25 09:00:00', '2026-06-25 11:10:00', 180, 'Scheduled'),
(DEFAULT, 2, 'Bangalore', 'Hyderabad', '2026-06-26 08:30:00', '2026-06-26 09:45:00', 160, 'Boarding');

INSERT INTO bookings VALUES
(DEFAULT, 1, 1, DEFAULT, '12A', 5500.00, 'Confirmed', NULL, DEFAULT),
(DEFAULT, 2, 2, DEFAULT, '8B', 3200.00, 'Confirmed', NULL, DEFAULT);

INSERT INTO baggage VALUES
(DEFAULT, 1, 18.50, 'Checked-In', DEFAULT, DEFAULT),
(DEFAULT, 2, 7.00, 'Checked-In', DEFAULT, DEFAULT);

INSERT INTO staff VALUES
(DEFAULT, 'spider-man', 'Pilot', '+91-9000011111', 'spidey@airport.com'),
(DEFAULT, 'MJ', 'Cabin Crew', '+91-9000022222', 'mj@airport.com');

INSERT INTO trip_staff VALUES
(1, 1, 'Pilot'),
(2, 2, 'Cabin Crew');