DROP DATABASE IF EXISTS airport_management;
CREATE DATABASE airport_management;
USE airport_management;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL
);

CREATE TABLE passengers (
    passenger_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    passport_number VARCHAR(30) UNIQUE,
    nationality VARCHAR(50)
);

CREATE TABLE flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_number VARCHAR(20) NOT NULL UNIQUE,
    airline_name VARCHAR(50)
);

CREATE TABLE trips (
    trip_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_id INT NOT NULL,
    source_airport VARCHAR(50) NOT NULL,
    destination_airport VARCHAR(50) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    total_seats INT NOT NULL,
    status ENUM('Scheduled', 'Boarding', 'Delayed', 'Cancelled', 'Completed') DEFAULT 'Scheduled',

    FOREIGN KEY (flight_id) REFERENCES flights(flight_id),
    
    CHECK (arrival_time > departure_time),
    CHECK (total_seats > 0),
    CHECK (source_airport <> destination_airport)
);

CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    passenger_id INT NOT NULL,
    trip_id INT NOT NULL,
    booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    seat_number VARCHAR(10),
    fare DECIMAL(10,2) NOT NULL,
    booking_status ENUM('Confirmed', 'Cancelled', 'No-Show') DEFAULT 'Confirmed',
    cancelled_at DATETIME,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (passenger_id) REFERENCES passengers(passenger_id),
    FOREIGN KEY (trip_id) REFERENCES trips(trip_id),

    UNIQUE (trip_id, seat_number),
    
    CHECK(fare>=0)
);

CREATE TABLE baggage (
    baggage_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL,
    weight DECIMAL(5,2) NOT NULL,
    baggage_status ENUM('Checked-In', 'Loaded', 'Lost', 'Delivered') DEFAULT 'Checked-In',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),
    
    CHECK(weight>=0)
);

CREATE TABLE staff (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE trip_staff (
    trip_id INT NOT NULL,
    staff_id INT NOT NULL,
    assigned_role VARCHAR(50),

    PRIMARY KEY (trip_id, staff_id),

    FOREIGN KEY (trip_id) REFERENCES trips(trip_id),
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);


