package motorph.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainMenuFrame extends JFrame {
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton payrollButton;
    private JButton testsButton;
    private JButton reportsButton;
    private JButton exitButton;
    private JButton backButton;

    public MainMenuFrame() {
        setTitle("MotorPH Payroll System - Main Menu");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
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

        titleLabel = new JLabel("MotorPH Payroll System");
        titleLabel.setBounds(150, 40, 300, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Main Menu");
        subtitleLabel.setBounds(150, 75, 300, 20);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel);

        payrollButton = createMenuButton("💰 Run Payroll System", new Color(52, 152, 219), 175, 130);
        testsButton = createMenuButton("🧪 Run System Tests", new Color(155, 89, 182), 175, 190);
        reportsButton = createMenuButton("📊 Run Reports", new Color(46, 204, 113), 175, 250);
        exitButton = createMenuButton("🚪 Exit System", new Color(231, 76, 60), 175, 310);
        backButton = createMenuButton("🔙 Back to Dashboard", new Color(149, 165, 166), 175, 370);

        mainPanel.add(payrollButton);
        mainPanel.add(testsButton);
        mainPanel.add(reportsButton);
        mainPanel.add(exitButton);
        mainPanel.add(backButton);

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
        payrollButton.addActionListener(e -> runPayrollSystem());
        testsButton.addActionListener(e -> runSystemTests());
        reportsButton.addActionListener(e -> runReports());
        exitButton.addActionListener(e -> exitSystem());
        backButton.addActionListener(e -> {
            dispose();
            new MainDashboard("user"); // Added user role parameter
        });
    }

    private void runPayrollSystem() {
        JOptionPane.showMessageDialog(this,
                "Payroll System is running!\n" +
                        "(This would execute your payroll processing logic)",
                "Payroll System",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void runSystemTests() {
        new SystemTestsFrame(this);
    }

    private void runReports() {
        new ReportsFrame(this);
    }

    private void exitSystem() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit the system?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuFrame());
    }
}

class SystemTestsFrame extends JFrame {
    private JPanel mainPanel;
    private JButton statutoryButton;
    private JButton payrollButton;
    private JButton hoursButton;
    private JButton backButton;

    public SystemTestsFrame(JFrame parent) {
        setTitle("MotorPH - System Tests");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
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

        JLabel titleLabel = new JLabel("System Tests");
        titleLabel.setBounds(150, 40, 300, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        statutoryButton = createMenuButton("📝 Statutory Deductions Tests", new Color(52, 152, 219), 175, 120);
        payrollButton = createMenuButton("💰 Payroll Processing Tests", new Color(155, 89, 182), 175, 180);
        hoursButton = createMenuButton("⏱️ Work Hours Calculator Tests", new Color(46, 204, 113), 175, 240);
        backButton = createMenuButton("🔙 Back to Main Menu", new Color(149, 165, 166), 175, 300);

        mainPanel.add(statutoryButton);
        mainPanel.add(payrollButton);
        mainPanel.add(hoursButton);
        mainPanel.add(backButton);

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
        statutoryButton.addActionListener(e -> runStatutoryTests());
        payrollButton.addActionListener(e -> runPayrollTests());
        hoursButton.addActionListener(e -> runHoursTests());
        backButton.addActionListener(e -> dispose());
    }

    private void runStatutoryTests() {
        JOptionPane.showMessageDialog(this,
                "Running Statutory Deductions Tests...\n" +
                        "(This would execute your statutory deductions test logic)",
                "Statutory Tests",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void runPayrollTests() {
        JOptionPane.showMessageDialog(this,
                "Running Payroll Processing Tests...\n" +
                        "(This would execute your payroll processing test logic)",
                "Payroll Tests",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void runHoursTests() {
        JOptionPane.showMessageDialog(this,
                "Running Work Hours Calculator Tests...\n" +
                        "(This would execute your work hours calculator test logic)",
                "Hours Tests",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

class ReportsFrame extends JFrame {
    private JPanel mainPanel;
    private JButton weeklyHoursButton;
    private JButton holidayPayButton;
    private JButton backButton;

    public ReportsFrame(JFrame parent) {
        setTitle("MotorPH - Reports");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
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

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setBounds(150, 40, 300, 35);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel);

        weeklyHoursButton = createMenuButton("📅 Weekly Hours Report", new Color(52, 152, 219), 175, 150);
        holidayPayButton = createMenuButton("🎉 Holiday Pay Report", new Color(155, 89, 182), 175, 210);
        backButton = createMenuButton("🔙 Back to Main Menu", new Color(149, 165, 166), 175, 270);

        mainPanel.add(weeklyHoursButton);
        mainPanel.add(holidayPayButton);
        mainPanel.add(backButton);

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
        weeklyHoursButton.addActionListener(e -> runWeeklyHoursReport());
        holidayPayButton.addActionListener(e -> runHolidayPayReport());
        backButton.addActionListener(e -> dispose());
    }

    private void runWeeklyHoursReport() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(new Color(0xF5DEB3));

        JTextField employeeIdField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();

        inputPanel.add(new JLabel("Employee ID:"));
        inputPanel.add(employeeIdField);
        inputPanel.add(new JLabel("Start Date (MM/DD/YYYY):"));
        inputPanel.add(startDateField);
        inputPanel.add(new JLabel("End Date (MM/DD/YYYY):"));
        inputPanel.add(endDateField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Weekly Hours Report Parameters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Generating Weekly Hours Report for:\n" +
                            "Employee ID: " + employeeIdField.getText() + "\n" +
                            "From: " + startDateField.getText() + " to " + endDateField.getText(),
                    "Weekly Hours Report",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void runHolidayPayReport() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(new Color(0xF5DEB3));

        JTextField yearField = new JTextField();
        JTextField monthField = new JTextField();

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Month (1-12):"));
        inputPanel.add(monthField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel,
                "Holiday Pay Report Parameters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "Generating Holiday Pay Report for:\n" +
                            "Year: " + yearField.getText() + "\n" +
                            "Month: " + monthField.getText(),
                    "Holiday Pay Report",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}