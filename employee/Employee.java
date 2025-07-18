package motorph.employee;

public class Employee {
    // Employee basic details
    private String employeeId;       // Employee ID
    private String lastName;         // Last name
    private String firstName;        // First name
    private String birthday;         // Birthday
    private String address;          // Home address
    private String phoneNumber;      // Contact number
    private String sssNo;            // SSS number
    private String philhealthNo;     // PhilHealth number
    private String tinNo;            // TIN number
    private String pagibigNo;        // Pag-IBIG number
    private String status;           // Employment status
    private String position;         // Job position
    private String immediateSupervisor; // Supervisor name

    // Salary information
    private double basicSalary;      // Monthly salary
    private double riceSubsidy;      // Monthly rice subsidy benefit
    private double phoneAllowance;   // Monthly phone allowance
    private double clothingAllowance; // Monthly clothing allowance
    private double grossSemiMonthlyRate; // Semi-monthly rate
    private double hourlyRate;       // Hourly rate

    /**
     * Create employee from CSV data array
     *
     * @param data Array of strings containing employee data from CSV
     */
    public Employee(String[] data) {
        // Check if we have enough data
        if (data.length >= 19) {
            this.employeeId = data[0].trim();
            this.lastName = data[1].trim();
            this.firstName = data[2].trim();
            this.birthday = data[3].trim();
            this.address = data[4].trim();
            this.phoneNumber = data[5].trim();
            this.sssNo = data[6].trim();
            this.philhealthNo = data[7].trim();
            this.tinNo = data[8].trim();
            this.pagibigNo = data[9].trim();
            this.status = data[10].trim();
            this.position = data[11].trim();
            this.immediateSupervisor = data[12].trim();

            // Parse number values
            this.basicSalary = parseDouble(data[13].trim());
            this.riceSubsidy = parseDouble(data[14].trim());
            this.phoneAllowance = parseDouble(data[15].trim());
            this.clothingAllowance = parseDouble(data[16].trim());
            this.grossSemiMonthlyRate = parseDouble(data[17].trim());
            this.hourlyRate = parseDouble(data[18].trim());
        } else {
            System.out.println("Warning: Not enough employee data, only " + data.length + " fields");
            // Set minimal data
            this.employeeId = data.length > 0 ? data[0].trim() : "";
            this.lastName = data.length > 1 ? data[1].trim() : "";
            this.firstName = data.length > 2 ? data[2].trim() : "";
        }
    }

    /**
     * Convert string to number with proper error handling
     *
     * @param value String value to convert
     * @return Parsed double value, or 0 if parsing fails
     */
    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }

        try {
            // Remove currency signs and commas
            String cleanValue = value
                    .replace("₱", "")
                    .replace("P", "")
                    .replace(",", "")
                    .replace(" ", "")
                    .trim();

            // Check if empty after cleaning
            if (cleanValue.isEmpty()) {
                return 0.0;
            }

            // Parse number
            return Double.parseDouble(cleanValue);
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + value);
            return 0.0;
        }
    }

    // Getters and Setters for employee details
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSssNo() { return sssNo; }
    public void setSssNo(String sssNo) { this.sssNo = sssNo; }

    public String getPhilhealthNo() { return philhealthNo; }
    public void setPhilhealthNo(String philhealthNo) { this.philhealthNo = philhealthNo; }

    public String getTinNo() { return tinNo; }
    public void setTinNo(String tinNo) { this.tinNo = tinNo; }

    public String getPagibigNo() { return pagibigNo; }
    public void setPagibigNo(String pagibigNo) { this.pagibigNo = pagibigNo; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSupervisor() { return immediateSupervisor; }
    public void setSupervisor(String immediateSupervisor) { this.immediateSupervisor = immediateSupervisor; }

    // Salary getters and setters
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getHourlyRate() {
        if (hourlyRate == 0.0 && basicSalary > 0.0) {
            return basicSalary / (22 * 8); // 22 working days, 8 hours per day
        }
        return hourlyRate;
    }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getDailyRate() {
        return basicSalary / 22;
    }

    public double getSemiMonthlyRate() {
        if (grossSemiMonthlyRate > 0) {
            return grossSemiMonthlyRate;
        }
        return basicSalary / 2;
    }
    public void setSemiMonthlyRate(double grossSemiMonthlyRate) { this.grossSemiMonthlyRate = grossSemiMonthlyRate; }

    public double getRiceSubsidy() { return riceSubsidy; }
    public void setRiceSubsidy(double riceSubsidy) { this.riceSubsidy = riceSubsidy; }

    public double getPhoneAllowance() { return phoneAllowance; }
    public void setPhoneAllowance(double phoneAllowance) { this.phoneAllowance = phoneAllowance; }

    public double getClothingAllowance() { return clothingAllowance; }
    public void setClothingAllowance(double clothingAllowance) { this.clothingAllowance = clothingAllowance; }

    public double getTotalBenefits() {
        return riceSubsidy + phoneAllowance + clothingAllowance;
    }

    /**
     * Converts employee data to CSV format string
     * @return String in CSV format containing employee data
     */
    public String toCSVString() {
        return String.join(",",
                employeeId,
                lastName,
                firstName,
                sssNo,
                philhealthNo,
                tinNo,
                pagibigNo
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Employee ID: ").append(employeeId).append("\n");
        sb.append("Name: ").append(firstName).append(" ").append(lastName).append("\n");
        sb.append("Position: ").append(position).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("\nSalary Information:\n");
        sb.append("  Basic Salary: ₱").append(String.format("%,.2f", basicSalary)).append("\n");
        sb.append("  Hourly Rate: ₱").append(String.format("%.2f", getHourlyRate())).append("\n");
        sb.append("\nID Numbers:\n");
        sb.append("  SSS: ").append(sssNo).append("\n");
        sb.append("  PhilHealth: ").append(philhealthNo).append("\n");
        sb.append("  Pag-IBIG: ").append(pagibigNo).append("\n");
        sb.append("  TIN: ").append(tinNo).append("\n");
        sb.append("\nMonthly Benefits:\n");
        sb.append("  Rice Subsidy: ₱").append(String.format("%,.2f", riceSubsidy)).append("\n");
        sb.append("  Phone Allowance: ₱").append(String.format("%,.2f", phoneAllowance)).append("\n");
        sb.append("  Clothing Allowance: ₱").append(String.format("%,.2f", clothingAllowance)).append("\n");
        sb.append("  Total Benefits: ₱").append(String.format("%,.2f", getTotalBenefits()));

        return sb.toString();
    }
}