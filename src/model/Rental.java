package model;

import java.util.Date;

public class Rental {

    private int id;
    private int userId;
    private int carId;
    private Date rentalDate;
    private String status; // ACTIVE, RETURNED

    // Extra fields for UI display
    private String carDetails;
    private String userName;

    public Rental(){}

    public Rental(int id, int userId, int carId, Date rentalDate, String status) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.rentalDate = rentalDate;
        this.status = status;
    }

    public int getId(){ return id; }

    public void setId(int id){ this.id = id; }

    public int getUserId(){ return userId; }

    public void setUserId(int userId){ this.userId = userId; }

    public int getCarId(){ return carId; }

    public void setCarId(int carId){ this.carId = carId; }

    public Date getRentalDate(){ return rentalDate; }

    public void setRentalDate(Date rentalDate){ this.rentalDate = rentalDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getCarDetails() { return carDetails; }

    public void setCarDetails(String carDetails) { this.carDetails = carDetails; }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }
}