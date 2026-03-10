package ui;

import model.Rental;
import repository.RentalRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ManageRentalsPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final RentalRepository rentalRepository;

    public ManageRentalsPanel() {
        this.rentalRepository = new RentalRepository();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Rental ID", "Customer", "Car Details", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton refreshBtn = new JButton("Refresh");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadAllRentals());

        loadAllRentals();
    }

    private void loadAllRentals() {
        tableModel.setRowCount(0);
        try {
            List<Rental> rentals = rentalRepository.getAllRentals();
            for (Rental rental : rentals) {
                tableModel.addRow(new Object[]{
                        rental.getId(),
                        rental.getUserName(),
                        rental.getCarDetails(),
                        rental.getRentalDate(),
                        rental.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rentals: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
