package repository;

import database.DBConnection;
import model.Rental;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository {

    // Add a new rental record to the database
    public void addRental(Rental rental) throws SQLException {
        String sql = "INSERT INTO rentals (user_id, car_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rental.getUserId());
            stmt.setInt(2, rental.getCarId());
            stmt.setString(3, rental.getStatus());
            stmt.executeUpdate();
        }
    }

    // Fetch all rentals associated with a specific user
    public List<Rental> getRentalsByUserId(int userId) throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, c.make, c.model FROM rentals r " +
                     "JOIN cars c ON r.car_id = c.car_id " +
                     "WHERE r.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rental rental = new Rental(
                            rs.getInt("rental_id"),
                            rs.getInt("user_id"),
                            rs.getInt("car_id"),
                            rs.getTimestamp("rental_date"),
                            rs.getString("status")
                    );
                    rental.setCarDetails(rs.getString("make") + " " + rs.getString("model"));
                    rentals.add(rental);
                }
            }
        }
        return rentals;
    }

    // Fetch all rental records in the system (Admin only)
    public List<Rental> getAllRentals() throws SQLException {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT r.*, c.make, c.model, u.name FROM rentals r " +
                     "JOIN cars c ON r.car_id = c.car_id " +
                     "JOIN users u ON r.user_id = u.user_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Rental rental = new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("user_id"),
                        rs.getInt("car_id"),
                        rs.getTimestamp("rental_date"),
                        rs.getString("status")
                );
                rental.setCarDetails(rs.getString("make") + " " + rs.getString("model"));
                rental.setUserName(rs.getString("name"));
                rentals.add(rental);
            }
        }
        return rentals;
    }

    // Mark a rental as returned by updating its status
    public void returnCar(int rentalId) throws SQLException {
        String sql = "UPDATE rentals SET status = 'RETURNED' WHERE rental_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rentalId);
            stmt.executeUpdate();
        }
    }
    
    // Retrieve the car ID associated with a specific rental record
    public int getCarIdByRentalId(int rentalId) throws SQLException {
        String sql = "SELECT car_id FROM rentals WHERE rental_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, rentalId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("car_id");
                }
            }
        }
        return -1;
    }
}
