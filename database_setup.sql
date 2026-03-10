-- Database schema for Car Rental System
CREATE DATABASE IF NOT EXISTS car_rental;
USE car_rental;

-- User accounts table
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

-- Vehicle inventory table
CREATE TABLE IF NOT EXISTS cars (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    daily_rate DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

-- Rental transactions table
CREATE TABLE IF NOT EXISTS rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    rental_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'RETURNED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
);

-- Initial administrative account
INSERT IGNORE INTO users (name, username, password, role) VALUES ('Administrator', 'admin', 'admin', 'ADMIN');

-- Sample vehicle data
INSERT IGNORE INTO cars (make, model, year, daily_rate, is_available) VALUES 
('Toyota', 'Vios', 2022, 1500.00, TRUE),
('Honda', 'Civic', 2023, 2500.00, TRUE),
('Ford', 'Ranger', 2021, 3000.00, TRUE),
('Mitsubishi', 'Mirage', 2020, 1200.00, TRUE),
('Nissan', 'Navara', 2022, 2800.00, TRUE);
