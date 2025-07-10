package motorph.gui;

import motorph.employee.Employee;
import motorph.employee.EmployeeDataReader;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class EmployeeManagement extends JFrame {
    private EmployeeDataReader employeeDataReader;
    private EmployeeOperationsManager operationsManager;
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton viewEmployeeButton, newEmployeeButton, updateEmployeeButton, deleteEmployeeButton, backButton;

    private static final String[] COLUMN_NAMES = {
            "Employee Number", "Last Name", "First Name", "Position", "Status",
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number", "Actions"
    };

    private static final Color BACKGROUND_COLOR = new Color(0xF5DEB3);
    private static final Color HEADER_BACKGROUND = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER = new Color(230, 240, 250);

    public EmployeeManagement() {
        initializeFrame();
        createComponents();
        loadEmployeeData();
        setupButtonListeners();
    }

    private void initializeFrame() {
        setTitle("MotorPH Employee Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
    }

    private void createComponents() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Employee Database Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(51, 51, 51));
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createTablePanel() {
        initializeTableModel();
        initializeTable();

        scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void initializeTableModel() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COLUMN_NAMES.length - 1;
            }
        };
    }

    private void initializeTable() {
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(40);
        employeeTable.setFont(new Font("Arial", Font.PLAIN, 12));

        configureTableHeader();
        configureColumnWidths();
        setupActionColumn();

        employeeTable.setGridColor(new Color(230, 230, 230));
        employeeTable.setBackground(Color.WHITE);
        employeeTable.setSelectionBackground(BUTTON_HOVER);

        // Add double-click handler
        employeeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedEmployee();
                }
            }
        });
    }

    private void configureTableHeader() {
        JTableHeader header = employeeTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(HEADER_BACKGROUND);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
    }

    private void configureColumnWidths() {
        int[] widths = {100, 120, 120, 150, 100, 120, 120, 120, 120};
        for (int i = 0; i < widths.length; i++) {
            employeeTable.getColumn(COLUMN_NAMES[i]).setPreferredWidth(widths[i]);
        }
    }

    private void setupActionColumn() {
        employeeTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        employeeTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));
        employeeTable.getColumn("Actions").setPreferredWidth(120);
        employeeTable.getColumn("Actions").setMaxWidth(120);
        employeeTable.getColumn("Actions").setMinWidth(120);
    }

    private JPanel createButtonPanel() {
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

        setInitialButtonStates();

        return buttonPanel;
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

        button.addMouseListener(new MouseAdapter() {
            final Color originalColor = bgColor;

            public void mouseEntered(MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor.darker());
                }
            }

            public void mouseExited(MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(originalColor);
                }
            }
        });

        return button;
    }

    private void setInitialButtonStates() {
        viewEmployeeButton.setEnabled(false);
        updateEmployeeButton.setEnabled(false);
        deleteEmployeeButton.setEnabled(false);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            boolean hasSelection = employeeTable.getSelectedRow() != -1;
            viewEmployeeButton.setEnabled(hasSelection);
            updateEmployeeButton.setEnabled(hasSelection);
            deleteEmployeeButton.setEnabled(hasSelection);
        });
    }

    private void loadEmployeeData() {
        tableModel.setRowCount(0);

        try {
            String csvFile = "Employee Data.csv";
            employeeDataReader = new EmployeeDataReader(csvFile);
            operationsManager = new EmployeeOperationsManager(csvFile, this);

            List<Employee> employees = employeeDataReader.getAllEmployees();
            for (Employee employee : employees) {
                tableModel.addRow(createEmployeeRowData(employee));
            }

            showSuccessMessage(employees.size());
        } catch (Exception e) {
            handleLoadError(e);
        }
    }

    private Object[] createEmployeeRowData(Employee employee) {
        return new Object[]{
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
    }

    private void showSuccessMessage(int employeeCount) {
        JOptionPane.showMessageDialog(this,
                "Employee data loaded successfully!\nTotal employees: " + employeeCount,
                "Data Loaded Successfully",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLoadError(Exception e) {
        addDummyData();
        JOptionPane.showMessageDialog(this,
                "Could not load employee CSV file: " + e.getMessage() + "\n" +
                        "Showing sample data instead.",
                "File Not Found",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private void addDummyData() {
        Object[][] sampleData = {
                {"001", "Dela Cruz", "Juan", "Manager", "Regular", "12-3456789-0", "1234567890", "123-456-789", "1234567890", "View/Edit"},
                {"002", "Santos", "Maria", "Supervisor", "Regular", "12-3456789-1", "1234567891", "123-456-790", "1234567891", "View/Edit"}
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private void setupButtonListeners() {
        viewEmployeeButton.addActionListener(e -> viewSelectedEmployee());
        newEmployeeButton.addActionListener(e -> createNewEmployee());
        updateEmployeeButton.addActionListener(e -> viewSelectedEmployee()); // Reuse
        deleteEmployeeButton.addActionListener(e -> deleteSelectedEmployee());
        backButton.addActionListener(e -> dispose());
    }

    private void viewSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow >= 0) {
            String employeeId = (String) tableModel.getValueAt(selectedRow, 0);
            Employee employee = operationsManager.getEmployeeDataReader().getEmployee(employeeId);

            if (employee != null) {
                new EmployeeDetailsFrame(employee).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Employee details could not be loaded",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createNewEmployee() {
        new NewEmployeeForm(this);
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
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this,
                        "Employee " + employeeId + " has been deleted.",
                        "Deletion Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void refreshEmployeeData() {
        tableModel.setRowCount(0);
        loadEmployeeData();
    }

    public void refreshTable() {
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
            setFont(new Font("Arial", Font.BOLD, 11));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            setFocusPainted(false);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            if (isSelected) {
                setBackground(new Color(41, 128, 185));
            } else {
                setBackground(new Color(52, 152, 219));
            }
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
                employeeTable.setRowSelectionInterval(currentRow, currentRow);
                viewSelectedEmployee();
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
        SwingUtilities.invokeLater(() -> new EmployeeManagement().setVisible(true));
    }
}