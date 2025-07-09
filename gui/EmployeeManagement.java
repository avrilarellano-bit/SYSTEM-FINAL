package motorph.gui;

import motorph.employee.Employee;
import motorph.employee.EmployeeDataReader;
import motorph.deductions.StatutoryDeductions;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class EmployeeManagement extends JFrame {
    private EmployeeDataReader employeeDataReader;
    private EmployeeOperationsManager operationsManager;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton viewEmployeeButton, newEmployeeButton, updateEmployeeButton, deleteEmployeeButton, backButton;

    private final String[] columnNames = {
            "Employee Number", "Last Name", "First Name", "Position", "Status",
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number", "Actions"
    };

    public EmployeeManagement() {
        setTitle("MotorPH Employee Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0xF5DEB3));

        createComponents();
        loadEmployeeData();
        setupButtons();
    }

    private void createComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Employee Database Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        headerPanel.add(titleLabel);

        // Table Setup
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columnNames.length - 1; // Only the last column (Actions) is editable
            }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(40);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        employeeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeeTable.getTableHeader().setBackground(new Color(70, 130, 180));
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.getTableHeader().setPreferredSize(new Dimension(0, 35));
        employeeTable.setGridColor(new Color(230, 230, 230));
        employeeTable.setBackground(Color.WHITE);
        employeeTable.setSelectionBackground(new Color(230, 240, 250));

        // Configure columns
        employeeTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        employeeTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        employeeTable.getColumn("Actions").setPreferredWidth(120);
        employeeTable.getColumn("Actions").setMaxWidth(120);
        employeeTable.getColumn("Actions").setMinWidth(120);

        // Set column widths
        employeeTable.getColumn("Employee Number").setPreferredWidth(100);
        employeeTable.getColumn("Last Name").setPreferredWidth(120);
        employeeTable.getColumn("First Name").setPreferredWidth(120);
        employeeTable.getColumn("Position").setPreferredWidth(150);
        employeeTable.getColumn("Status").setPreferredWidth(100);
        employeeTable.getColumn("SSS Number").setPreferredWidth(120);
        employeeTable.getColumn("PhilHealth Number").setPreferredWidth(120);
        employeeTable.getColumn("TIN").setPreferredWidth(120);
        employeeTable.getColumn("Pag-IBIG Number").setPreferredWidth(120);

        scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(0xF5DEB3));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        viewEmployeeButton = createStyledButton("View Selected Employee", new Color(52, 152, 219));
        newEmployeeButton = createStyledButton("New Employee", new Color(46, 204, 113));
        updateEmployeeButton = createStyledButton("Update Employee", new Color(241, 196, 15));
        deleteEmployeeButton = createStyledButton("Delete Employee", new Color(231, 76, 60));
        backButton = createStyledButton("Back to Dashboard", new Color(149, 165, 166));

        buttonPanel.add(viewEmployeeButton);
        buttonPanel.add(newEmployeeButton);
        buttonPanel.add(updateEmployeeButton);
        buttonPanel.add(deleteEmployeeButton);
        buttonPanel.add(backButton);

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Initial button states
        viewEmployeeButton.setEnabled(false);
        updateEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);

        // Selection listener
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = employeeTable.getSelectedRow() != -1;
            viewEmployeeButton.setEnabled(hasSelection);
            updateEmployeeButton.setEnabled(hasSelection);
            deleteEmployeeButton.setEnabled(hasSelection);
        });
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(160, 35));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = bgColor;

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor.darker());
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor);
                }
            }
        });

        return button;
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);

        try {
            String csvFile = "util/Employee Data.csv";  // Updated path
            employeeDataReader = new EmployeeDataReader(csvFile);
            operationsManager = new EmployeeOperationsManager(csvFile, this);

            List<Employee> employees = employeeDataReader.getAllEmployees();

            for (Employee employee : employees) {
                Object[] rowData = {
                        employee.getEmployeeId(),
                        employee.getLastName(),
                        employee.getFirstName(),
                        employee.getPosition(),
                        employee.getStatus(),
                        employee.getSssNo(),
                        employee.getPhilhealthNo(),
                        employee.getTinNo(),
                        employee.getPagibigNo(),
                        "View/Edit"
                };
                tableModel.addRow(rowData);
            }

            JOptionPane.showMessageDialog(this,
                    "Employee data loaded successfully!\n" +
                            "Total employees: " + employees.size(),
                    "Data Loaded Successfully",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            addDummyData();
            JOptionPane.showMessageDialog(this,
                    "Could not load employee CSV file: " + e.getMessage() + "\n" +
                            "Showing sample data instead.",
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addDummyData() {
        Object[][] sampleData = {
                {"001", "Dela Cruz", "Juan", "Manager", "Regular", "12-3456789-0", "1234567890", "123-456-789", "1234567890", "View/Edit"},
                {"002", "Santos", "Maria", "Supervisor", "Regular", "12-3456789-1", "1234567891", "123-456-790", "1234567891", "View/Edit"},
                {"003", "Garcia", "Pedro", "Staff", "Probationary", "12-3456789-2", "1234567892", "123-456-791", "1234567892", "View/Edit"},
                {"004", "Rodriguez", "Ana", "Staff", "Regular", "12-3456789-3", "1234567893", "123-456-792", "1234567893", "View/Edit"},
                {"005", "Martinez", "Carlos", "Staff", "Probationary", "12-3456789-4", "1234567894", "123-456-793", "1234567894", "View/Edit"}
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void setupButtons() {
        viewEmployeeButton.addActionListener(e -> viewSelectedEmployee());
        newEmployeeButton.addActionListener(e -> createNewEmployee());
        updateEmployeeButton.addActionListener(e -> updateSelectedEmployee());
        deleteEmployeeButton.addActionListener(e -> deleteSelectedEmployee());
        backButton.addActionListener(e -> {
            dispose();
        });
    }

    private void viewSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            showEmployeeDetails(selectedRow);
        }
    }

    private void createNewEmployee() {
        new NewEmployeeForm(this);
    }

    private void updateSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
            showEmployeeDetails(selectedRow);
        }
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete employee " + employeeId + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // TODO: Implement actual deletion logic
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this,
                        "Employee " + employeeId + " has been deleted.",
                        "Deletion Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void showEmployeeDetails(int row) {
        String employeeId = (String) tableModel.getValueAt(row, 0);
        String lastName = (String) tableModel.getValueAt(row, 1);
        String firstName = (String) tableModel.getValueAt(row, 2);

        // TODO: Implement detailed employee view
        JOptionPane.showMessageDialog(this,
                "Employee Details:\n" +
                        "ID: " + employeeId + "\n" +
                        "Name: " + lastName + ", " + firstName + "\n" +
                        "\nDetailed information would be shown here.",
                "Employee Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshEmployeeData() {
        tableModel.setRowCount(0);
        loadEmployeeData();
    }

    public void refreshTable() {
        // Preserve the current selection if needed
        int selectedRow = employeeTable.getSelectedRow();
        refreshEmployeeData();
        if (selectedRow >= 0 && selectedRow < employeeTable.getRowCount()) {
            employeeTable.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 10));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            currentRow = row;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                showEmployeeDetails(currentRow);
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EmployeeManagement().setVisible(true);
        });
    }
}