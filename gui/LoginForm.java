package motorph.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginForm extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton, adminLoginButton;
    private JLabel messageLabel;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD_HASH = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD_HASH = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

    public LoginForm() {
        setTitle("MotorPH Payroll System - Login");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(0xF5DEB3));
        setLayout(new BorderLayout());

        createComponents();
        setupButtons();
        setVisible(true);
    }

    private void createComponents() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(0xF5DEB3));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setBounds(90, 30, 280, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Employee Management System");
        subtitleLabel.setBounds(90, 65, 280, 20);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        JLabel demoLabel = new JLabel("Demo: admin/password or user/123456");
        demoLabel.setBounds(90, 90, 280, 20);
        demoLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        demoLabel.setForeground(new Color(128, 128, 128));
        demoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(demoLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 140, 80, 25);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(51, 51, 51));
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 140, 180, 30);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField.setBackground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 180, 80, 25);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(51, 51, 51));
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 180, 180, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField.setBackground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(passwordField);

        loginButton = createStyledButton("Login", new Color(46, 204, 113));
        loginButton.setBounds(110, 230, 100, 35);
        mainPanel.add(loginButton);

        cancelButton = createStyledButton("Cancel", new Color(231, 76, 60));
        cancelButton.setBounds(220, 230, 100, 35);
        mainPanel.add(cancelButton);

        // Updated Admin Login Button to match other buttons' style
        adminLoginButton = createStyledButton("Admin Login", new Color(52, 152, 219));
        adminLoginButton.setBounds(110, 280, 210, 35);
        mainPanel.add(adminLoginButton);

        messageLabel = new JLabel("");
        messageLabel.setBounds(70, 330, 280, 25);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel);

        JLabel securityLabel = new JLabel("Passwords are securely hashed with SHA-256");
        securityLabel.setBounds(70, 360, 280, 20);
        securityLabel.setFont(new Font("Arial", Font.ITALIC, 9));
        securityLabel.setForeground(new Color(100, 150, 100));
        securityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(securityLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    private void setupButtons() {
        loginButton.addActionListener(e -> authenticateUser());
        cancelButton.addActionListener(e -> System.exit(0));
        passwordField.addActionListener(e -> authenticateUser());
        adminLoginButton.addActionListener(e -> {
            this.dispose();
            new AdminLoginForm();
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in both fields!", Color.RED);
            return;
        }

        try {
            boolean authenticated = false;

            if ((username.equals("admin") && password.equals("password")) ||
                    (username.equals("user") && password.equals("123456"))) {
                authenticated = true;
            }
            else if (username.equals(ADMIN_USERNAME) &&
                    hashPassword(password).equals(ADMIN_PASSWORD_HASH)) {
                authenticated = true;
            }
            else if (username.equals(USER_USERNAME) &&
                    hashPassword(password).equals(USER_PASSWORD_HASH)) {
                authenticated = true;
            }

            if (authenticated) {
                showMessage("Login successful! Redirecting...", new Color(46, 204, 113));
                new Timer(1500, e -> {
                    dispose();
                    new MainDashboard(username);
                }).start();
            } else {
                showMessage("Invalid credentials!", Color.RED);
                passwordField.setText("");
            }
        } catch (Exception ex) {
            showMessage("Authentication error: " + ex.getMessage(), Color.RED);
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}