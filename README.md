# Hotel Reservation Management System

A secure and modular Hotel Reservation Management System developed using Core Java, JDBC, and MySQL. The application implements CRUD operations, database connectivity, input validation, and SQL Injection protection using PreparedStatement and industry-standard JDBC practices.

## Features

- Reserve a Room
- View Reservations
- Get Room Number by Reservation ID
- Update Reservation Details
- Delete Reservations
- Contact Number Validation
- SQL Injection Protection using PreparedStatement
- Auto-generated Reservation IDs
- Timestamp-based Reservation Records

## Technologies Used

- Core Java
- JDBC
- MySQL
- PreparedStatement
- Exception Handling
- IntelliJ IDEA
- Git & GitHub

## Database Schema

```sql
CREATE DATABASE hotel_db;

USE hotel_db;

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(100) NOT NULL,
    room_no INT NOT NULL,
    contact_number VARCHAR(10) NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Project Structure

```
HotelReservationSystem
│
├── src
│   └── HotelReservationSystem.java
├── database.sql
├── README.md
└── .gitignore
```

## Author

Raj Pandey
