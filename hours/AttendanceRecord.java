package motorph.hours;

import motorph.util.DateTimeUtil;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;

public class AttendanceRecord {
    private String employeeId;
    private String lastName;
    private String firstName;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private LocalDateTime dateTimeIn;

    public static final LocalTime STANDARD_START_TIME = LocalTime.of(8, 0);
    public static final LocalTime GRACE_PERIOD_END = LocalTime.of(8, 10);
    public static final LocalTime STANDARD_END_TIME = LocalTime.of(17, 0);
    public static final int LUNCH_BREAK_MINUTES = 60;

    public AttendanceRecord(String[] data) {
        if (data.length >= 6) {
            this.employeeId = data[0].trim();
            this.lastName = data[1].trim();
            this.firstName = data[2].trim();
            this.date = DateTimeUtil.parseDate(data[3].trim());
            this.timeIn = DateTimeUtil.parseTime(data[4].trim());
            this.timeOut = DateTimeUtil.parseTime(data[5].trim());
        } else {
            System.out.println("Warning: Not enough attendance data fields");
        }
    }

    public AttendanceRecord() {
        this.employeeId = "";
        this.lastName = "";
        this.firstName = "";
        this.date = LocalDate.now();
        this.timeIn = LocalTime.of(0, 0);
        this.timeOut = LocalTime.of(0, 0);
    }

    public boolean isLate() {
        return timeIn != null && timeIn.isAfter(GRACE_PERIOD_END);
    }

    public boolean isUndertime() {
        return timeOut != null && timeOut.isBefore(STANDARD_END_TIME);
    }

    public double getLateMinutes() {
        if (!isLate() || timeIn == null) {
            return 0.0;
        }
        Duration lateBy = Duration.between(GRACE_PERIOD_END, timeIn);
        return lateBy.toMinutes();
    }

    public double getUndertimeMinutes() {
        if (!isUndertime() || timeOut == null) {
            return 0.0;
        }
        Duration undertimeBy = Duration.between(timeOut, STANDARD_END_TIME);
        return undertimeBy.toMinutes();
    }

    public double getTotalHoursWorked() {
        if (timeIn == null || timeOut == null || timeOut.isBefore(timeIn)) {
            return 0.0;
        }

        LocalTime effectiveTimeOut = timeOut;
        if (isLate() && timeOut.isAfter(STANDARD_END_TIME)) {
            effectiveTimeOut = STANDARD_END_TIME;
        }

        Duration workDuration = Duration.between(timeIn, effectiveTimeOut);
        double totalMinutes = workDuration.toMinutes();

        if (totalMinutes >= 300) {
            totalMinutes -= LUNCH_BREAK_MINUTES;
        }

        double hours = totalMinutes / 60.0;
        return Math.round(hours * 100) / 100.0;
    }

    public double getRegularHoursWorked() {
        return Math.min(getTotalHoursWorked(), 8.0);
    }

    public double getOvertimeHours() {
        if (isLate()) {
            return 0.0;
        }

        if (timeOut == null || !timeOut.isAfter(STANDARD_END_TIME)) {
            return 0.0;
        }

        Duration overtimeDuration = Duration.between(STANDARD_END_TIME, timeOut);
        double overtimeHours = overtimeDuration.toMinutes() / 60.0;

        return Math.round(overtimeHours * 100) / 100.0;
    }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFullName() { return firstName + " " + lastName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTimeIn() { return timeIn; }
    public void setTimeIn(LocalTime timeIn) { this.timeIn = timeIn; }

    public LocalTime getTimeOut() { return timeOut; }
    public void setTimeOut(LocalTime timeOut) { this.timeOut = timeOut; }

    public LocalDateTime getDateTimeIn() { return dateTimeIn; }
    public void setDateTimeIn(LocalDateTime time) { this.dateTimeIn = time; }

    public String getFormattedDate() {
        return DateTimeUtil.formatDateStandard(date);
    }

    public String getFormattedTimeIn() {
        return DateTimeUtil.formatTimeStandard(timeIn);
    }

    public String getFormattedTimeOut() {
        return DateTimeUtil.formatTimeStandard(timeOut);
    }

    @Override
    public String toString() {
        return "Date: " + getFormattedDate() +
                ", Employee: " + getFullName() +
                ", Time In: " + getFormattedTimeIn() +
                ", Time Out: " + getFormattedTimeOut();
    }
}