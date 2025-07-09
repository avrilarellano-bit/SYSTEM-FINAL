package motorph.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class AdminLoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPanel mainPanel;
    private JLabel messageLabel;

    public AdminLoginForm() {
        setTitle("MotorPH - Admin Login");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(0xF5DEB3));

        createComponents();
        setVisible(true);
    }

    private void createComponents() {
        mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(0xF5DEB3));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Admin Login");
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

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 190, 100, 35);
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.addActionListener(e -> attemptLogin());
        mainPanel.add(loginButton);

        createAdminRegistrationButton();

        messageLabel = new JLabel("");
        messageLabel.setBounds(70, 240, 280, 25);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(messageLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createAdminRegistrationButton() {
        JButton registerButton = new JButton("Register New Admin");
        registerButton.setBounds(110, 280, 200, 25);
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setForeground(new Color(70, 130, 180));
        registerButton.setBorder(BorderFactory.createEmptyBorder());
        registerButton.setContentAreaFilled(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                registerButton.setForeground(new Color(30, 100, 150));
            }
            public void mouseExited(MouseEvent evt) {
                registerButton.setForeground(new Color(70, 130, 180));
            }
        });

        registerButton.addActionListener(e -> {
            new AdminRegistration();
            this.setVisible(false);
        });

        mainPanel.add(registerButton);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty() || password.length == 0) {
            showMessage("Please enter both username and password", Color.RED);
            return;
        }

        // In a real application, you would verify against stored credentials
        // This is just a placeholder for demonstration
        showMessage("Login attempt for: " + username, new Color(46, 204, 113));
        Arrays.fill(password, '0'); // Clear the password for security
    }

    public static void fillCredentials(String username, String password) {
        for (Window window : Window.getWindows()) {
            if (window instanceof AdminLoginForm) {
                AdminLoginForm loginForm = (AdminLoginForm) window;
                loginForm.usernameField.setText(username);
                loginForm.passwordField.setText(password);
                loginForm.setVisible(true);
                loginForm.toFront();
                break;
            }
        }
    }

    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLoginForm());
    }
}