package ui;

import model.User;
import repository.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// Admin panel for monitoring registered users
public class ManageUsersPanel extends JPanel {

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final UserRepository userRepository;

    public ManageUsersPanel() {
        this.userRepository = new UserRepository();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"User ID", "Name", "Username", "Role"};
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

        refreshBtn.addActionListener(e -> loadUsers());

        loadUsers();
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        try {
            List<User> users = userRepository.getAllUsers();
            for (User user : users) {
                tableModel.addRow(new Object[]{
                        user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getRole()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
