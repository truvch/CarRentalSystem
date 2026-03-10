-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS car_rental;
USE car_rental;

-- Table for users (Customers and Admins)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL
);

-- Table for cars
CREATE TABLE IF NOT EXISTS cars (
    car_id INT AUTO_INCREMENT PRIMARY KEY,
    make VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    daily_rate DECIMAL(10, 2) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

-- Table for rentals
CREATE TABLE IF NOT EXISTS rentals (
    rental_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    rental_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'RETURNED') DEFAULT 'ACTIVE',
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (car_id) REFERENCES cars(car_id)
);

-- Insert initial admin user
INSERT INTO users (name, username, password, role) VALUES ('System Admin', 'admin', 'admin123', 'ADMIN');

-- Insert some sample cars
INSERT INTO cars (make, model, year, daily_rate, is_available) VALUES 
('Toyota', 'Camry', 2022, 50.00, TRUE),
('Honda', 'Civic', 2021, 45.00, TRUE),
('Ford', 'Mustang', 2023, 100.00, TRUE),
('Tesla', 'Model 3', 2022, 120.00, TRUE),
('BMW', 'X5', 2021, 150.00, TRUE);
