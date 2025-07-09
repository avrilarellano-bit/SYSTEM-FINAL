package motorph.hours;

import java.time.LocalTime;
import java.time.Duration;
import java.time.LocalDateTime;

public class WorkHoursCalculator {
    private static final LocalTime STANDARD_START_TIME = AttendanceRecord.STANDARD_START_TIME;
    private static final LocalTime STANDARD_END_TIME = AttendanceRecord.STANDARD_END_TIME;
    private static final LocalTime GRACE_PERIOD_END = AttendanceRecord.GRACE_PERIOD_END;
    private static final double REGULAR_HOURS = 8.0;

    public double calculateHoursWorked(LocalTime timeIn, LocalTime timeOut, boolean isLate) {
        if (timeIn == null || timeOut == null || timeOut.isBefore(timeIn)) {
            return 0.0;
        }

        Duration workDuration = Duration.between(timeIn, timeOut);
        double hoursWorked = workDuration.toMinutes() / 60.0;
        return Math.min(hoursWorked, REGULAR_HOURS);
    }

    public double calculateOvertimeHours(LocalTime timeOut, boolean isLate) {
        if (isLate || timeOut == null || !timeOut.isAfter(STANDARD_END_TIME)) {
            return 0.0;
        }
        return Duration.between(STANDARD_END_TIME, timeOut).toMinutes() / 60.0;
    }

    public double computeRegularHours(LocalDateTime timeIn, LocalDateTime timeOut) {
        Duration duration = Duration.between(timeIn, timeOut);
        return Math.min(duration.toHours(), 8.0);
    }

    public double computeOvertime(LocalDateTime timeIn, LocalDateTime timeOut) {
        double totalHours = Duration.between(timeIn, timeOut).toHours();
        return Math.max(totalHours - 8.0, 0);
    }
}