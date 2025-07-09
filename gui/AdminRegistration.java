package motorph.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AdminRegistration extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, cancelButton;
    private JLabel messageLabel;

    public AdminRegistration() {
        setTitle("MotorPH - Admin Registration");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

        JLabel titleLabel = new JLabel("Admin Registration");
        titleLabel.setBounds(90, 30, 280, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(70, 100, 80, 25);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabel.setForeground(new Color(51, 51, 51));
        mainPanel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 100, 180, 30);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(70, 140, 80, 25);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(new Color(51, 51, 51));
        mainPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 140, 180, 30);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm:");
        confirmLabel.setBounds(70, 180, 80, 25);
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 12));
        confirmLabel.setForeground(new Color(51, 51, 51));
        mainPanel.add(confirmLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(150, 180, 180, 30);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(confirmPasswordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(110, 240, 100, 35);
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(registerButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(220, 240, 100, 35);
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(cancelButton);

        messageLabel = new JLabel("");
        messageLabel.setBounds(70, 290, 280, 25);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupButtons() {
        registerButton.addActionListener(e -> registerAdmin());
        cancelButton.addActionListener(e -> dispose());
    }

    private void registerAdmin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields!", Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match!", Color.RED);
            return;
        }

        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters!", Color.RED);
            return;
        }

        try {
            String hashedPassword = hashPassword(password);
            // In a real application, you would save this to a database
            showMessage("Registration successful!", new Color(46, 204, 113));

            // Clear fields after successful registration
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");

        } catch (NoSuchAlgorithmException ex) {
            showMessage("Error during registration: " + ex.getMessage(), Color.RED);
        }
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

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminRegistration());
    }
}