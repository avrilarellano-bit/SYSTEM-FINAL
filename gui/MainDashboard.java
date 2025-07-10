package motorph.gui;

import motorph.process.PayrollProcessor;
import motorph.test.*;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainDashboard extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel, subtitleLabel;
    private JButton employeeButton, payrollButton, reportsButton, logoutButton;
    private final AtomicBoolean isProcessing = new AtomicBoolean(false);
    private String currentUserRole;

    public MainDashboard(String userRole) {
        this.currentUserRole = userRole;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("MotorPH Payroll System - Dashboard (" + currentUserRole + ")");
        setSize(600, 500);
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        welcomeLabel = new JLabel("Welcome to MotorPH");
        welcomeLabel.setBounds(150, 40, 300, 35);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(70, 130, 180));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeLabel);

        subtitleLabel = new JLabel("Payroll Management System");
        subtitleLabel.setBounds(150, 75, 300, 20);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        employeeButton = createMenuButton("👥 Employee Management", new Color(52, 152, 219), 175, 130);
        payrollButton = createMenuButton("💰 Payroll Processing", new Color(46, 204, 113), 175, 190);
        reportsButton = createMenuButton("📊 Reports & Analytics", new Color(155, 89, 182), 175, 250);
        logoutButton = createMenuButton("🔒 Logout", new Color(231, 76, 60), 175, 310);

        mainPanel.add(employeeButton);
        mainPanel.add(payrollButton);
        mainPanel.add(reportsButton);
        mainPanel.add(logoutButton);

        JLabel footerLabel = new JLabel("Select an option to continue");
        footerLabel.setBounds(150, 430, 300, 20);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        footerLabel.setForeground(new Color(149, 165, 166));
        footerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(footerLabel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text, Color bgColor, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 250, 50);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    private void setupButtons() {
        // ✅ Updated Employee Management Button with Admin Access Check
        employeeButton.addActionListener(e -> {
            if (!currentUserRole.equals("admin")) {
                JOptionPane.showMessageDialog(this,
                        "Only administrators can access Employee Management",
                        "Access Denied",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            dispose();
            new EmployeeManagement().setVisible(true);
        });

        // Payroll Processing Button
        payrollButton.addActionListener(e -> {
            dispose();
            new MainMenuFrame(); // Opens the main menu with payroll options
        });

        // Reports Button
        reportsButton.addActionListener(e -> {
            JDialog reportsDialog = new JDialog(this, "Reports Menu", true);
            reportsDialog.setSize(400, 300);
            reportsDialog.setLocationRelativeTo(this);
            reportsDialog.setLayout(new GridLayout(4, 1, 10, 10));
            reportsDialog.getContentPane().setBackground(new Color(0xF5DEB3));

            JButton holidayReportBtn = createStyledButton("Holiday Pay Report", new Color(52, 152, 219));
            JButton statutoryReportBtn = createStyledButton("Statutory Deductions Report", new Color(155, 89, 182));
            JButton workHoursReportBtn = createStyledButton("Work Hours Report", new Color(46, 204, 113));
            JButton closeBtn = createStyledButton("Close", new Color(231, 76, 60));

            holidayReportBtn.addActionListener(ev -> {
                new HolidayPayTest().runTests();
                JOptionPane.showMessageDialog(reportsDialog,
                        "Holiday Pay Report generated successfully!",
                        "Report Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            statutoryReportBtn.addActionListener(ev -> {
                new StatutoryDeductionsTest().runTests();
                JOptionPane.showMessageDialog(reportsDialog,
                        "Statutory Deductions Report generated successfully!",
                        "Report Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            workHoursReportBtn.addActionListener(ev -> {
                new WorkHoursCalculatorTest().runTests();
                JOptionPane.showMessageDialog(reportsDialog,
                        "Work Hours Report generated successfully!",
                        "Report Complete",
                        JOptionPane.INFORMATION_MESSAGE);
            });

            closeBtn.addActionListener(ev -> reportsDialog.dispose());

            reportsDialog.add(holidayReportBtn);
            reportsDialog.add(statutoryReportBtn);
            reportsDialog.add(workHoursReportBtn);
            reportsDialog.add(closeBtn);
            reportsDialog.setVisible(true);
        });

        // Logout Button
        logoutButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new LoginForm();
            }
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Starting MainDashboard...");
            new MainDashboard("admin");
        });
    }
}
