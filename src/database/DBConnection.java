package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/car_rental";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() {

        try {

            return DriverManager.getConnection(URL, USER, PASS);

        } catch (Exception e) {

            System.out.println("Database not connected yet.");
            return null;

        }
    }

}