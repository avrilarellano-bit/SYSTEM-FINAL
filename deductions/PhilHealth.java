package motorph.deductions;

public class PhilHealth implements DeductionProvider {
    private static final double RATE = 0.03;
    private static final double MAX_CONTRIBUTION = 1800.0;
    private static final double MIN_SALARY_THRESHOLD = 10000.0;
    private static final double MIN_CONTRIBUTION = 300.0;

    @Override
    public double calculateContribution(double monthlySalary) {
        if (monthlySalary <= MIN_SALARY_THRESHOLD) {
            return MIN_CONTRIBUTION;
        } else if (monthlySalary <= 60000) {
            return monthlySalary * RATE;
        }
        return MAX_CONTRIBUTION;
    }

    @Override
    public String getName() {
        return "PhilHealth";
    }

    @Override
    public boolean appliesTo(int payPeriodType) {
        return payPeriodType == DeductionProvider.MID_MONTH;
    }

    public double calculateSemiMonthlyContribution(double monthlySalary) {
        return calculateContribution(monthlySalary) / 2;
    }
}