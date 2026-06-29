CREATE DATABASE IF NOT EXISTS hotel_db;

USE hotel_db;

CREATE TABLE IF NOT EXISTS reservations (
                                            reservation_id INT PRIMARY KEY AUTO_INCREMENT,
                                            guest_name VARCHAR(100) NOT NULL,
    room_no INT NOT NULL,
    contact_number VARCHAR(10) NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );