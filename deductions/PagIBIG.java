package motorph.deductions;

public class PagIBIG implements DeductionProvider {
    private static final double RATE_LOWER = 0.01;
    private static final double RATE_HIGHER = 0.02;
    private static final double MIN_SALARY = 1000.0;
    private static final double MID_SALARY = 1500.0;
    private static final double MAX_CONTRIBUTION = 100.0;
    private static final double MIN_CONTRIBUTION = 100.0;

    @Override
    public double calculateContribution(double monthlySalary) {
        if (monthlySalary < MIN_SALARY) {
            return 0.0;
        }

        double contribution;
        if (monthlySalary <= MID_SALARY) {
            contribution = monthlySalary * RATE_LOWER;
        } else {
            contribution = monthlySalary * RATE_HIGHER;
        }

        contribution = Math.max(contribution, MIN_CONTRIBUTION);
        contribution = Math.min(contribution, MAX_CONTRIBUTION);

        return contribution;
    }

    @Override
    public String getName() {
        return "Pag-IBIG";
    }

    @Override
    public boolean appliesTo(int payPeriodType) {
        return payPeriodType == DeductionProvider.MID_MONTH;
    }

    public double computeContribution(double monthlySalary) {
        if (monthlySalary <= MID_SALARY) {
            return monthlySalary * RATE_LOWER;
        } else {
            return Math.min(monthlySalary * RATE_HIGHER, MAX_CONTRIBUTION);
        }
    }
}