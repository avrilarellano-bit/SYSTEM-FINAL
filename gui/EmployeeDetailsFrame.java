package motorph.gui;

import motorph.employee.Employee;
import motorph.process.PayPeriod;
import motorph.process.PayrollDateManager;
import motorph.process.PayrollProcessor;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Map;

public class EmployeeDetailsFrame extends JFrame {
    private Employee employee;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> periodTypeComboBox;
    private JButton computeButton;
    private JButton backButton;
    private JTextArea detailsArea;
    private PayrollProcessor payrollProcessor;

    public EmployeeDetailsFrame(Employee employee) {
        this.employee = employee;
        this.payrollProcessor = new PayrollProcessor("data/employees.csv", "data/attendance.csv");
        setTitle("Employee Details - " + employee.getFullName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        layoutComponents();
        displayEmployeeDetails();
    }

    private void initializeComponents() {
        // Month selection
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthComboBox = new JComboBox<>(months);
        monthComboBox.setSelectedIndex(Month.values()[LocalDate.now().getMonthValue() - 1].ordinal());

        // Year selection
        yearComboBox = new JComboBox<>();
        int currentYear = Year.now().getValue();
        for (int year = currentYear - 1; year <= currentYear + 1; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(currentYear);

        // Pay period type selection
        String[] periodTypes = {"First Half (1st-15th)", "Second Half (16th-End)"};
        periodTypeComboBox = new JComboBox<>(periodTypes);

        // Compute button
        computeButton = new JButton("Compute Payroll");
        computeButton.addActionListener(e -> computePayroll());

        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        // Details area
        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        detailsArea.setMargin(new Insets(10, 10, 10, 10));
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel for controls
        JPanel controlPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        // First row - month/year selection
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.add(new JLabel("Month:"));
        datePanel.add(monthComboBox);
        datePanel.add(new JLabel("Year:"));
        datePanel.add(yearComboBox);

        // Second row - period type and buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.add(new JLabel("Pay Period:"));
        actionPanel.add(periodTypeComboBox);
        actionPanel.add(computeButton);
        actionPanel.add(backButton);

        controlPanel.add(datePanel);
        controlPanel.add(actionPanel);

        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);

        add(panel);
    }

    private void displayEmployeeDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EMPLOYEE DETAILS ===\n\n");
        sb.append(String.format("%-20s: %s\n", "Employee ID", employee.getEmployeeId()));
        sb.append(String.format("%-20s: %s\n", "Full Name", employee.getFullName()));
        sb.append(String.format("%-20s: %s\n", "Position", employee.getPosition()));
        sb.append(String.format("%-20s: %s\n", "Status", employee.getStatus()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Monthly Salary", employee.getBasicSalary()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Semi-Monthly Rate", employee.getSemiMonthlyRate()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Daily Rate", employee.getDailyRate()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Hourly Rate", employee.getHourlyRate()));

        sb.append("\n=== BENEFITS ===\n");
        sb.append(String.format("%-20s: %s\n", "SSS Number", employee.getSssNo()));
        sb.append(String.format("%-20s: %s\n", "PhilHealth Number", employee.getPhilhealthNo()));
        sb.append(String.format("%-20s: %s\n", "TIN", employee.getTinNo()));
        sb.append(String.format("%-20s: %s\n", "Pag-IBIG Number", employee.getPagibigNo()));

        sb.append("\n=== ALLOWANCES ===\n");
        sb.append(String.format("%-20s: ₱%,.2f\n", "Rice Subsidy", employee.getRiceSubsidy()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Phone Allowance", employee.getPhoneAllowance()));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Clothing Allowance", employee.getClothingAllowance()));

        sb.append("\nSelect a pay period and click 'Compute Payroll' to view salary details.");

        detailsArea.setText(sb.toString());
    }

    private void computePayroll() {
        int selectedMonth = monthComboBox.getSelectedIndex() + 1;
        int selectedYear = (int) yearComboBox.getSelectedItem();
        int payPeriodType = periodTypeComboBox.getSelectedIndex() == 0 ?
                PayrollDateManager.MID_MONTH : PayrollDateManager.END_MONTH;

        try {
            // Get payroll date and cutoff period
            LocalDate payrollDate = PayrollDateManager.getPayrollDate(selectedYear, selectedMonth, payPeriodType);
            LocalDate[] cutoffDates = PayrollDateManager.getCutoffDateRange(payrollDate, payPeriodType);

            // Generate pay period
            PayPeriod payPeriod = new PayPeriod(cutoffDates[0], cutoffDates[1], payrollDate,
                    payPeriodType == PayrollDateManager.MID_MONTH ? PayPeriod.FIRST_HALF : PayPeriod.SECOND_HALF);

            // Process payroll (simplified - in real app you'd get actual attendance data)
            PayrollProcessor.PayrollResult result = payrollProcessor.processPayroll(
                    employee,
                    96, // 12 days * 8 hours (simplified)
                    5,  // 5 overtime hours
                    30, // 30 minutes late
                    0,  // 0 undertime minutes
                    true, // was late
                    payPeriodType,
                    payPeriod.getStartDate(),
                    payPeriod.getEndDate(),
                    selectedYear,
                    selectedMonth,
                    false // no unpaid absences
            );

            displayPayrollResults(result, payPeriod);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error processing payroll: " + e.getMessage(),
                    "Payroll Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void displayPayrollResults(PayrollProcessor.PayrollResult result, PayPeriod payPeriod) {
        StringBuilder sb = new StringBuilder();

        // Employee info
        sb.append("=== PAYROLL DETAILS ===\n\n");
        sb.append(String.format("%-20s: %s\n", "Employee", result.employee.getFullName()));
        sb.append(String.format("%-20s: %s\n", "Employee ID", result.employee.getEmployeeId()));
        sb.append(String.format("%-20s: %s\n", "Position", result.employee.getPosition()));

        // Pay period info
        sb.append("\n=== PAY PERIOD ===\n");
        sb.append(String.format("%-20s: %s to %s\n", "Cutoff Period",
                PayrollDateManager.formatDate(payPeriod.getStartDate()),
                PayrollDateManager.formatDate(payPeriod.getEndDate())));
        sb.append(String.format("%-20s: %s\n", "Pay Date",
                PayrollDateManager.formatDate(payPeriod.getPayDate())));
        sb.append(String.format("%-20s: %s\n", "Period Type",
                payPeriod.getPeriodType() == PayPeriod.FIRST_HALF ? "First Half" : "Second Half"));

        // Earnings
        sb.append("\n=== EARNINGS ===\n");
        sb.append(String.format("%-20s: ₱%,.2f\n", "Base Pay", result.basePay));
        sb.append(String.format("%-20s: ₱%,.2f (%s hours)\n", "Overtime Pay",
                result.overtimePay, result.overtimeHours));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Holiday Pay", result.holidayPay));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Gross Pay", result.grossPay));

        // Deductions
        sb.append("\n=== DEDUCTIONS ===\n");
        if (result.lateDeduction > 0) {
            sb.append(String.format("%-20s: ₱%,.2f (%s minutes)\n", "Late Deduction",
                    result.lateDeduction, result.lateMinutes));
        }
        if (result.undertimeDeduction > 0) {
            sb.append(String.format("%-20s: ₱%,.2f (%s minutes)\n", "Undertime Deduction",
                    result.undertimeDeduction, result.undertimeMinutes));
        }
        if (result.absenceDeduction > 0) {
            sb.append(String.format("%-20s: ₱%,.2f\n", "Absence Deduction", result.absenceDeduction));
        }
        sb.append(String.format("%-20s: ₱%,.2f\n", "SSS", result.deductions.sssDeduction));
        sb.append(String.format("%-20s: ₱%,.2f\n", "PhilHealth", result.deductions.philhealthDeduction));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Pag-IBIG", result.deductions.pagibigDeduction));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Withholding Tax", result.deductions.withholdingTax));
        sb.append(String.format("%-20s: ₱%,.2f\n", "Total Deductions", result.deductions.totalDeductions));

        // Net Pay
        sb.append("\n=== NET PAY ===\n");
        sb.append(String.format("%-20s: ₱%,.2f\n", "Net Salary", result.netPay));

        // Work details
        sb.append("\n=== WORK DETAILS ===\n");
        sb.append(String.format("%-20s: %s hours\n", "Hours Worked", result.hoursWorked));
        sb.append(String.format("%-20s: %s hours\n", "Expected Hours", result.expectedHours));
        sb.append(String.format("%-20s: %s hours\n", "Absent Hours", result.absentHours));

        detailsArea.setText(sb.toString());
    }
}