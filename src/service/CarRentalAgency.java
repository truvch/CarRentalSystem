package service;

import exception.CarRentalException;
import model.Car;
import model.Rental;
import repository.CarRepository;
import repository.RentalRepository;

import java.sql.SQLException;
import java.util.List;

public class CarRentalAgency {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    public CarRentalAgency() {
        this.carRepository = new CarRepository();
        this.rentalRepository = new RentalRepository();
    }

    // Add a new car with basic validation
    public void addCar(Car car) throws CarRentalException {
        validateCar(car);
        try {
            carRepository.addCar(car);
        } catch (SQLException e) {
            throw new CarRentalException("Error adding car: " + e.getMessage());
        }
    }

    // Retrieve the full inventory of cars
    public List<Car> displayCars() throws CarRentalException {
        try {
            return carRepository.getAllCars();
        } catch (SQLException e) {
            throw new CarRentalException("Error fetching cars: " + e.getMessage());
        }
    }

    // Retrieve only cars that can be rented
    public List<Car> displayAvailableCars() throws CarRentalException {
        try {
            return carRepository.getAvailableCars();
        } catch (SQLException e) {
            throw new CarRentalException("Error fetching available cars: " + e.getMessage());
        }
    }

    // Process a car rental transaction for a user
    public void rentCar(int userId, int carId) throws CarRentalException {
        try {
            Car car = carRepository.getCarById(carId);
            if (car == null) throw new CarRentalException("Car not found.");
            if (!car.isAvailable()) throw new CarRentalException("Car is not available.");

            Rental rental = new Rental(0, userId, carId, null, "ACTIVE");
            rentalRepository.addRental(rental);
            carRepository.setAvailability(carId, false);
        } catch (SQLException e) {
            throw new CarRentalException("Error renting car: " + e.getMessage());
        }
    }

    // Process a car return and update its availability
    public void returnCar(int rentalId) throws CarRentalException {
        try {
            int carId = rentalRepository.getCarIdByRentalId(rentalId);
            if (carId == -1) throw new CarRentalException("Rental record not found.");
            
            rentalRepository.returnCar(rentalId);
            carRepository.setAvailability(carId, true);
        } catch (SQLException e) {
            throw new CarRentalException("Error returning car: " + e.getMessage());
        }
    }

    // Search for a specific car by its brand and model
    public Car findCar(String make, String model) throws CarRentalException {
        try {
            for (Car car : carRepository.getAllCars()) {
                if (car.getBrand().equalsIgnoreCase(make) && car.getModel().equalsIgnoreCase(model)) {
                    return car;
                }
            }
        } catch (SQLException e) {
            throw new CarRentalException("Error finding car: " + e.getMessage());
        }
        return null;
    }

    // Perform logical checks on car details before saving
    private void validateCar(Car car) throws CarRentalException {
        if (car.getBrand() == null || car.getBrand().isEmpty()) throw new CarRentalException("Brand cannot be empty.");
        if (car.getModel() == null || car.getModel().isEmpty()) throw new CarRentalException("Model cannot be empty.");
        if (car.getYear() < 1886) throw new CarRentalException("Invalid car year.");
        if (car.getDailyRate() <= 0) throw new CarRentalException("Daily rate must be positive.");
    }
}
