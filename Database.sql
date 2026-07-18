-- ============================================================
-- HOTEL RESERVATION MANAGEMENT SYSTEM
-- Database Setup Script
--
-- Database : MySQL
-- Project  : Hotel Reservation Management System
-- ============================================================


-- ============================================================
-- 1. CREATE DATABASE
-- ============================================================

CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;


-- ============================================================
-- 2. DROP EXISTING TABLES
-- ============================================================
-- Note:
-- This script creates a fresh database setup.
-- Existing reservation data will be deleted if the tables exist.

DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS rooms;


-- ============================================================
-- 3. CREATE ROOMS TABLE
-- ============================================================

CREATE TABLE rooms (

                       room_no INT PRIMARY KEY,

                       room_type VARCHAR(20) NOT NULL,

                       price_per_night DECIMAL(10, 2) NOT NULL,

                       is_available BOOLEAN NOT NULL DEFAULT TRUE

);


-- ============================================================
-- 4. CREATE RESERVATIONS TABLE
-- ============================================================

CREATE TABLE reservations (

                              reservation_id INT PRIMARY KEY AUTO_INCREMENT,

                              guest_name VARCHAR(100) NOT NULL,

                              contact_number VARCHAR(10) NOT NULL,

                              room_no INT NOT NULL,

                              check_in_date DATE NOT NULL,

                              check_out_date DATE NOT NULL,

                              total_amount DECIMAL(10, 2) NOT NULL,

                              payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                              reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_reservation_room
                                  FOREIGN KEY (room_no)
                                      REFERENCES rooms(room_no)

);


-- ============================================================
-- 5. INSERT STANDARD ROOMS
-- Price: Rs. 1500 per night
-- ============================================================

INSERT INTO rooms
(room_no, room_type, price_per_night, is_available)
VALUES
    (101, 'Standard', 1500.00, TRUE),
    (102, 'Standard', 1500.00, TRUE),
    (103, 'Standard', 1500.00, TRUE),
    (104, 'Standard', 1500.00, TRUE),
    (105, 'Standard', 1500.00, TRUE);


-- ============================================================
-- 6. INSERT DELUXE ROOMS
-- Price: Rs. 2500 per night
-- ============================================================

INSERT INTO rooms
(room_no, room_type, price_per_night, is_available)
VALUES
    (201, 'Deluxe', 2500.00, TRUE),
    (202, 'Deluxe', 2500.00, TRUE),
    (203, 'Deluxe', 2500.00, TRUE),
    (204, 'Deluxe', 2500.00, TRUE),
    (205, 'Deluxe', 2500.00, TRUE);


-- ============================================================
-- 7. INSERT SUITE ROOMS
-- Price: Rs. 4000 per night
-- ============================================================

INSERT INTO rooms
(room_no, room_type, price_per_night, is_available)
VALUES
    (301, 'Suite', 4000.00, TRUE),
    (302, 'Suite', 4000.00, TRUE),
    (303, 'Suite', 4000.00, TRUE);


-- ============================================================
-- 8. VERIFY DATABASE SETUP
-- ============================================================

-- Display all available tables
SHOW TABLES;


-- Display all rooms
SELECT * FROM rooms;


-- Display all reservations
SELECT * FROM reservations;