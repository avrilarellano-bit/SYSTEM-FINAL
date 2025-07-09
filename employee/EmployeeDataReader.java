package motorph.employee;

import java.io.IOException;
import java.util.*;

public class EmployeeDataReader {
    private final String employeeFilePath;
    private final Map<String, Employee> employeeMap;

    public EmployeeDataReader(String employeeFilePath) {
        this.employeeFilePath = employeeFilePath;
        this.employeeMap = new HashMap<>();
        loadEmployees();
    }

    private void loadEmployees() {
        try {
            List<String[]> allRows = CSVReader.read(employeeFilePath);

            for (String[] dataArray : allRows) {
                if (dataArray.length < 19) {
                    continue;
                }

                try {
                    Employee employee = new Employee(dataArray);
                    employeeMap.put(employee.getEmployeeId(), employee);
                } catch (Exception e) {
                    // Skip bad records
                    System.out.println("Skipping invalid employee record: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading employee file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error loading employees: " + e.getMessage());
        }
    }

    public Employee getEmployee(String employeeId) {
        return employeeMap.get(employeeId);
    }

    public Employee findEmployeeByName(String fullName) {
        String searchName = fullName.toLowerCase().trim();

        for (Employee employee : employeeMap.values()) {
            String empFullName = (employee.getFirstName() + " " + employee.getLastName()).toLowerCase();

            if (empFullName.equals(searchName) ||
                    empFullName.contains(searchName) ||
                    searchName.contains(empFullName)) {
                return employee;
            }
        }

        return null;
    }

    public Employee findEmployee(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return null;
        }

        Employee employee = getEmployee(searchTerm.trim());

        if (employee == null) {
            employee = findEmployeeByName(searchTerm);
        }

        return employee;
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }
}