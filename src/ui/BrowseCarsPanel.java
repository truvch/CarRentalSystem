package ui;

import exception.CarRentalException;
import model.Car;
import model.User;
import service.CarRentalAgency;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

// Panel for customers to browse and rent available cars
public class BrowseCarsPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TableRowSorter<DefaultTableModel> sorter;
    private final JTextField searchField;
    private final CarRentalAgency agency;
    private final User currentUser;

    public BrowseCarsPanel(User user) {
        this.currentUser = user;
        this.agency = new CarRentalAgency();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configure table with non-editable cells and typed columns for sorting
        String[] columns = {"ID", "Brand", "Model", "Year", "Daily Rate", "Availability"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 3) return Integer.class;
                if (columnIndex == 4) return Double.class;
                return String.class;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Search interface
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(0, 35));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel sLabel = new JLabel("Search Inventory: ");
        sLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(sLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton rentBtn = new JButton("RENT SELECTED CAR");
        rentBtn.setBackground(new Color(33, 150, 243));
        rentBtn.setForeground(Color.WHITE);
        rentBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        rentBtn.setFocusPainted(false);
        rentBtn.setPreferredSize(new Dimension(180, 40));
        rentBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.add(refreshBtn);
        buttonPanel.add(rentBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Hide internal ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        add(buttonPanel, BorderLayout.SOUTH);

        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Event listeners for searching and actions
        refreshBtn.addActionListener(e -> loadCars());
        rentBtn.addActionListener(e -> rentSelectedCar());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        loadCars();
    }

    // Refresh car list from database
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
                        car.isAvailable() ? "Available" : "Not Available"
                });
            }
        } catch (CarRentalException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Process rental request for the selected car
    private void rentSelectedCar() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a car to rent.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int carId = (int) table.getValueAt(selectedRow, 0);
        String availability = (String) table.getValueAt(selectedRow, 5);

        if (!"Available".equals(availability)) {
            JOptionPane.showMessageDialog(this, "This car is already rented.", "Not Available", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to rent " + table.getValueAt(selectedRow, 1) + " " + table.getValueAt(selectedRow, 2) + "?",
                "Confirm Rental", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                agency.rentCar(currentUser.getId(), carId);
                JOptionPane.showMessageDialog(this, "Rental successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCars();
            } catch (CarRentalException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Rental Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}