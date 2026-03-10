package ui;

import model.User;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private static final Color ADMIN_PRIMARY = new Color(45, 52, 54); // Dark Gray/Charcoal
    private static final Color ADMIN_ACCENT = new Color(0, 184, 148);  // Teal Green

    private final User currentUser;

    public AdminDashboard(User user) {
        this.currentUser = user;

        setTitle("Car Rental System - Administrator Dashboard");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Header Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(ADMIN_PRIMARY);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel brandLabel = new JLabel("CAR RENTAL ADMIN");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLabel.setForeground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Logged in as: " + currentUser.getName());
        welcomeLabel.setForeground(new Color(200, 200, 200));
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(brandLabel);
        infoPanel.add(welcomeLabel);

        JButton logoutBtn = new JButton("EXIT SYSTEM");
        logoutBtn.setBackground(new Color(255, 71, 87)); // Coral Red
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);

        tabs.addTab("  Inventory Management  ", new ManageCarsPanel());
        tabs.addTab("  System Rentals  ", new ManageRentalsPanel());
        tabs.addTab("  User Management  ", new ManageUsersPanel());

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}