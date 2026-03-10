package ui;

import exception.CarRentalException;
import model.Car;
import repository.CarRepository;
import service.CarRentalAgency;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Admin panel for CRUD operations on the car inventory
public class ManageCarsPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final CarRentalAgency agency;
    private final CarRepository carRepository;

    public ManageCarsPanel() {
        this.agency = new CarRentalAgency();
        this.carRepository = new CarRepository();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Brand", "Model", "Year", "Daily Rate", "Available"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Action buttons
        JButton addBtn = new JButton("ADD NEW CAR");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addBtn.setBackground(new Color(0, 184, 148));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton editBtn = new JButton("EDIT");
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(255, 71, 87));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners for car management
        addBtn.addActionListener(e -> showCarDialog(null));
        editBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int carId = (int) tableModel.getValueAt(selectedRow, 0);
                try {
                    Car car = carRepository.getCarById(carId);
                    showCarDialog(car);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error fetching car details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a car to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteBtn.addActionListener(e -> deleteCar());

        loadCars();
    }

    private void loadCars() {
        tableModel.setRowCount(0);
        try {
            List<Car> cars = agency.displayCars();
            for (Car car : cars) {
                tableModel.addRow(new Object[]{
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getYear(),
                        car.getDailyRate(),
                        car.isAvailable()
                });
            }
        } catch (CarRentalException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Modal dialog for adding or editing car details
    private void showCarDialog(Car car) {
        JTextField brandField = new JTextField(car != null ? car.getBrand() : "");
        JTextField modelField = new JTextField(car != null ? car.getModel() : "");
        JTextField yearField = new JTextField(car != null ? String.valueOf(car.getYear()) : "");
        JTextField rateField = new JTextField(car != null ? String.valueOf(car.getDailyRate()) : "");
        JCheckBox availableCheck = new JCheckBox("Is Available", car == null || car.isAvailable());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Brand:"));
        panel.add(brandField);
        panel.add(new JLabel("Model:"));
        panel.add(modelField);
        panel.add(new JLabel("Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Daily Rate:"));
        panel.add(rateField);
        panel.add(availableCheck);

        int result = JOptionPane.showConfirmDialog(this, panel, car == null ? "Add Car" : "Edit Car", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String brand = brandField.getText().trim();
                String modelName = modelField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                double rate = Double.parseDouble(rateField.getText().trim());
                boolean available = availableCheck.isSelected();

                if (car == null) {
                    agency.addCar(new Car(0, brand, modelName, year, rate, available));
                } else {
                    car.setBrand(brand);
                    car.setModel(modelName);
                    car.setYear(year);
                    car.setDailyRate(rate);
                    car.setAvailable(available);
                    carRepository.updateCar(car);
                }
                loadCars();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int carId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this car?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                carRepository.deleteCar(carId);
                loadCars();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting car: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}