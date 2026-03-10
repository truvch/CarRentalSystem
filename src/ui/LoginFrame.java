package ui;

import model.User;
import repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

// Main login screen for all users
public class LoginFrame extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color SECONDARY_COLOR = new Color(25, 118, 210);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    private static final Color ACCENT_COLOR = Color.WHITE;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private final UserRepository userRepository;

    public LoginFrame() {
        userRepository = new UserRepository();

        setTitle("Car Rental System - Login");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // UI Components setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("CAR RENTAL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);
        
        JLabel subtitleLabel = new JLabel("Welcome Back!", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        formPanel.setBackground(BACKGROUND_COLOR);

        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(0, 45));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 45));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userLabel.setForeground(TEXT_COLOR);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setForeground(TEXT_COLOR);

        formPanel.add(userLabel);
        formPanel.add(usernameField);
        formPanel.add(passLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setPreferredSize(new Dimension(0, 45));
        loginBtn.setBackground(PRIMARY_COLOR);
        loginBtn.setForeground(ACCENT_COLOR);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton registerBtn = new JButton("Don't have an account? Create one");
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setFocusPainted(false);
        registerBtn.setForeground(SECONDARY_COLOR);
        registerBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Action Listeners
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> openRegister());

        getRootPane().setDefaultButton(loginBtn);
    }

    // Handle authentication and dashboard redirection
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userRepository.login(username, password);
            if (user != null) {
                if ("ADMIN".equals(user.getRole())) {
                    new AdminDashboard(user).setVisible(true);
                } else {
                    new UserDashboard(user).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegister() {
        new RegisterFrame().setVisible(true);
    }
}