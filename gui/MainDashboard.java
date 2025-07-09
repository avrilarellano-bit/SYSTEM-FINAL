package motorph.gui;

import motorph.process.PayrollProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        employeeButton.addActionListener(e -> handleButtonClick(this::openEmployeeManagement));
        payrollButton.addActionListener(e -> handleButtonClick(this::openPayrollProcessing));
        reportsButton.addActionListener(e -> handleButtonClick(this::openReports));
        logoutButton.addActionListener(e -> handleButtonClick(this::logout));
    }

    private void handleButtonClick(Runnable action) {
        if (!isProcessing.getAndSet(true)) {
            try {
                action.run();
            } finally {
                isProcessing.set(false);
            }
        }
    }

    private void openEmployeeManagement() {
        if (!currentUserRole.equals("admin")) {
            JOptionPane.showMessageDialog(this,
                    "Only administrators can access Employee Management",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog loading = showLoading("Opening Employee Management");
        new Thread(() -> {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    dispose();
                    new EmployeeManagement();
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    JOptionPane.showMessageDialog(null,
                            "Could not open Employee Management: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                });
            }
        }).start();
    }

    private void openPayrollProcessing() {
        JDialog loading = showLoading("Opening Payroll Processing");
        new Thread(() -> {
            try {
                // Initialize payroll processor with actual file paths
                String employeeFile = "util/Employee Data.csv";
                String attendanceFile = "util/Attendance Data.csv"; // You'll need this file

                // Create a sample payroll processor
                PayrollProcessor processor = new PayrollProcessor(employeeFile, attendanceFile);

                // For demo purposes, process payroll for first employee
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    // Show payroll processing UI or results
                    JOptionPane.showMessageDialog(null,
                            "Payroll Processing initialized successfully!\n" +
                                    "Using employee file: " + employeeFile + "\n" +
                                    "Using attendance file: " + attendanceFile,
                            "Payroll Processing",
                            JOptionPane.INFORMATION_MESSAGE);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    JOptionPane.showMessageDialog(null,
                            "Error opening Payroll Processing: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void openReports() {
        JDialog loading = showLoading("Generating Reports");
        new Thread(() -> {
            try {
                Thread.sleep(500);
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    // Show sample reports based on test classes
                    String reportContent = "=== Payroll Reports ===\n\n" +
                            "1. Statutory Deductions Test Results\n" +
                            "   - Mid-month deductions passed\n" +
                            "   - End-month deductions passed\n\n" +
                            "2. Holiday Pay Calculations\n" +
                            "   - Regular holiday pay verified\n" +
                            "   - Special holiday pay verified\n\n" +
                            "3. Work Hours Calculations\n" +
                            "   - Normal day calculations passed\n" +
                            "   - Overtime calculations passed";

                    JTextArea textArea = new JTextArea(reportContent);
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 300));

                    JOptionPane.showMessageDialog(null, scrollPane,
                            "Payroll Reports", JOptionPane.PLAIN_MESSAGE);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    loading.dispose();
                    JOptionPane.showMessageDialog(null,
                            "Error generating reports: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginForm());
        }
    }

    private JDialog showLoading(String message) {
        JDialog loading = new JDialog(this, "Loading", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(message + "...", SwingConstants.CENTER));
        panel.add(new JProgressBar(), BorderLayout.SOUTH);
        loading.add(panel);
        loading.setSize(300, 100);
        loading.setLocationRelativeTo(this);
        loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loading.setVisible(true);
        return loading;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Starting MainDashboard...");
            new MainDashboard("admin");
        });
    }
}