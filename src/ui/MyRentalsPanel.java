package ui;

import model.Rental;
import model.User;
import repository.RentalRepository;
import service.CarRentalAgency;
import exception.CarRentalException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MyRentalsPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final RentalRepository rentalRepository;
    private final CarRentalAgency agency;
    private final User currentUser;

    public MyRentalsPanel(User user) {
        this.currentUser = user;
        this.rentalRepository = new RentalRepository();
        this.agency = new CarRentalAgency();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Rental ID", "Car Details", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton refreshBtn = new JButton("Refresh");
        JButton returnBtn = new JButton("Return Car");
        returnBtn.setBackground(new Color(63, 81, 181));
        returnBtn.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshBtn);
        buttonPanel.add(returnBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Hide Rental ID column from view but keep it for logic
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadRentals());
        returnBtn.addActionListener(e -> returnCar());

        loadRentals();
    }

    private void loadRentals() {
        tableModel.setRowCount(0);
        try {
            List<Rental> rentals = rentalRepository.getRentalsByUserId(currentUser.getId());
            for (Rental rental : rentals) {
                tableModel.addRow(new Object[]{
                        rental.getId(),
                        rental.getCarDetails(),
                        rental.getRentalDate(),
                        rental.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rentals: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnCar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a rental record to return.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rentalId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = (String) tableModel.getValueAt(selectedRow, 3);

        if ("RETURNED".equals(status)) {
            JOptionPane.showMessageDialog(this, "This car has already been returned.", "Already Returned", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to return this car?", "Confirm Return", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                agency.returnCar(rentalId);
                JOptionPane.showMessageDialog(this, "Car returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadRentals();
            } catch (CarRentalException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Return Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
