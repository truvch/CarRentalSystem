package ui;

import model.User;
import javax.swing.*;
import java.awt.*;

// Dashboard for customers to browse cars and view rentals
public class UserDashboard extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final User currentUser;

    public UserDashboard(User user) {
        this.currentUser = user;

        setTitle("Car Rental System - Customer Dashboard");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Header Panel with user info and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(PRIMARY_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel brandLabel = new JLabel("CAR RENTAL");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandLabel.setForeground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName() + " (" + currentUser.getUsername() + ")");
        welcomeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        welcomeLabel.setForeground(Color.WHITE);
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);
        infoPanel.add(brandLabel);
        infoPanel.add(welcomeLabel);

        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setBackground(new Color(255, 71, 87));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutBtn.setPreferredSize(new Dimension(100, 35));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);

        // Tabbed interface for browsing and history
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);

        tabs.addTab("  Browse Available Cars  ", new BrowseCarsPanel(currentUser));
        tabs.addTab("  My Rental History  ", new MyRentalsPanel(currentUser));

        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}