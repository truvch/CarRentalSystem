package exception;

// Custom exception class for car rental system business logic errors
public class CarRentalException extends Exception {

    public CarRentalException(String message) {
        super(message);
    }
}