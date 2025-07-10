package motorph.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class NewEmployeeForm extends JDialog {
    private EmployeeManagement parentWindow;

    JPanel mainPanel;
    JLabel titleLabel;

    JTextField employeeNumberField;
    JTextField firstNameField;
    JTextField lastNameField;
    JTextField birthdayField;
    JTextField addressField;
    JTextField phoneField;
    JTextField sssNumberField;
    JTextField philHealthNumberField;
    JTextField tinField;
    JTextField pagIbigNumberField;
    JTextField positionField;
    JTextField supervisorField;
    JTextField salaryField;

    JButton saveButton;
    JButton cancelButton;
    JLabel messageLabel;

    public NewEmployeeForm(EmployeeManagement parent) {
        super(parent, "Add New Employee", true);
        this.parentWindow = parent;

        setSize(550, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(0xF5DEB3));

        createComponents();
        setupButtons();
        setVisible(true);
    }

    void createComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        titleLabel = new JLabel("Add New Employee");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0xF5DEB3));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel subtitleLabel = new JLabel("Please fill in all employee information");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        mainPanel.add(subtitleLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        gbc.gridx = 0; gbc.gridy = row++;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        addSectionHeader("PERSONAL INFORMATION", row++);

        addFormRow("Employee Number:", employeeNumberField = new JTextField(20), row++);
        addFormRow("First Name:", firstNameField = new JTextField(20), row++);
        addFormRow("Last Name:", lastNameField = new JTextField(20), row++);
        addFormRow("Birthday (MM/DD/YYYY):", birthdayField = new JTextField(20), row++);
        addFormRow("Address:", addressField = new JTextField(20), row++);
        addFormRow("Phone Number:", phoneField = new JTextField(20), row++);

        gbc.gridx = 0; gbc.gridy = row++;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        addSectionHeader("GOVERNMENT IDENTIFICATION", row++);

        addFormRow("SSS Number:", sssNumberField = new JTextField(20), row++);
        addFormRow("PhilHealth Number:", philHealthNumberField = new JTextField(20), row++);
        addFormRow("TIN:", tinField = new JTextField(20), row++);
        addFormRow("Pag-IBIG Number:", pagIbigNumberField = new JTextField(20), row++);

        gbc.gridx = 0; gbc.gridy = row++;
        mainPanel.add(Box.createVerticalStrut(10), gbc);

        addSectionHeader("EMPLOYMENT DETAILS", row++);

        addFormRow("Position:", positionField = new JTextField(20), row++);
        addFormRow("Immediate Supervisor:", supervisorField = new JTextField(20), row++);
        addFormRow("Basic Salary (₱):", salaryField = new JTextField(20), row++);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(0xF5DEB3));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(0xF5DEB3));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        saveButton = createStyledButton("💾 Save Employee", new Color(46, 204, 113));
        cancelButton = createStyledButton("❌ Cancel", new Color(231, 76, 60));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(0xF5DEB3));
        messagePanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(0xF5DEB3));
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(messagePanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    void addSectionHeader(String title, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 10, 0);

        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel.setForeground(new Color(70, 130, 180));
        mainPanel.add(sectionLabel, gbc);
    }

    void addFormRow(String labelText, JTextField field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 15);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(51, 51, 51));
        label.setPreferredSize(new Dimension(180, 25));
        mainPanel.add(label, gbc);

        gbc.gridx = 1; gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        field.setFont(new Font("Arial", Font.PLAIN, 12));
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setPreferredSize(new Dimension(250, 35));

        mainPanel.add(field, gbc);
    }

    JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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

    void setupButtons() {
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEmployee();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(NewEmployeeForm.this,
                        "Are you sure you want to cancel?\nAny unsaved changes will be lost.",
                        "Confirm Cancel",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }

    void saveEmployee() {
        String employeeNumber = employeeNumberField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String birthday = birthdayField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String position = positionField.getText().trim();
        String supervisor = supervisorField.getText().trim();
        String salary = salaryField.getText().trim();
        String sssNumber = sssNumberField.getText().trim();
        String philHealthNumber = philHealthNumberField.getText().trim();
        String tin = tinField.getText().trim();
        String pagIbigNumber = pagIbigNumberField.getText().trim();

        if (employeeNumber.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            showMessage("Employee Number, First Name, and Last Name are required!", Color.RED);
            return;
        }

        try {
            Integer.parseInt(employeeNumber);
        } catch (NumberFormatException e) {
            showMessage("Employee Number must be a valid number!", Color.RED);
            return;
        }

        if (!salary.isEmpty()) {
            try {
                Double.parseDouble(salary);
            } catch (NumberFormatException e) {
                showMessage("Salary must be a valid number!", Color.RED);
                return;
            }
        }

        saveButton.setEnabled(false);
        saveButton.setText("Saving...");

        try {
            saveToCSV(employeeNumber, firstName, lastName, birthday, address, phone,
                    position, supervisor, salary, sssNumber, philHealthNumber, tin, pagIbigNumber);

            showMessage("Employee saved successfully! ✓", new Color(46, 204, 113));

            Timer timer = new Timer(1500, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (parentWindow != null) {
                        parentWindow.refreshTable();
                        JOptionPane.showMessageDialog(parentWindow,
                                "New employee '" + firstName + " " + lastName + "' has been added!\n" +
                                        "Employee ID: " + employeeNumber,
                                "Employee Added Successfully",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();

        } catch (IOException e) {
            showMessage("Error saving employee: " + e.getMessage(), Color.RED);
            e.printStackTrace();

            saveButton.setEnabled(true);
            saveButton.setText("💾 Save Employee");
        }
    }

    void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    void saveToCSV(String employeeNumber, String firstName, String lastName,
                   String birthday, String address, String phone, String position,
                   String supervisor, String salary, String sssNumber,
                   String philHealthNumber, String tin, String pagIbigNumber) throws IOException {

        String csvFile = "Employee Data.csv"; // Update path as needed
        File file = new File(csvFile);

        double monthlySalary = salary.isEmpty() ? 0 : Double.parseDouble(salary);
        double semiMonthlyRate = monthlySalary / 2;
        double hourlyRate = monthlySalary / (22 * 8);

        String csvLine = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"Regular\",\"%s\",\"%s\",\"%.2f\",\"1500.00\",\"2000.00\",\"1000.00\",\"%.2f\",\"%.2f\"",
                employeeNumber,
                lastName,
                firstName,
                birthday,
                address,
                phone,
                sssNumber,
                philHealthNumber,
                tin,
                pagIbigNumber,
                position,
                supervisor,
                monthlySalary,
                semiMonthlyRate,
                hourlyRate);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            if (file.length() == 0) {
                writer.write("Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate");
                writer.newLine();
            }
            writer.write(csvLine);
            writer.newLine();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewEmployeeForm(null));
    }
}
