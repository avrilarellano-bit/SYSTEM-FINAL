package motorph.deductions;

public class WithholdingTax implements DeductionProvider {
    private static final double BRACKET_0 = 20833;
    private static final double BRACKET_1 = 33332;
    private static final double BRACKET_2 = 66666;
    private static final double BRACKET_3 = 166666;
    private static final double BRACKET_4 = 666666;

    private static final double TAX_BRACKET_2 = 2500;
    private static final double TAX_BRACKET_3 = 10833;
    private static final double TAX_BRACKET_4 = 40833.33;
    private static final double TAX_BRACKET_5 = 200833.33;

    private static final double RATE_BRACKET_2 = 0.20;
    private static final double RATE_BRACKET_3 = 0.25;
    private static final double RATE_BRACKET_4 = 0.30;
    private static final double RATE_BRACKET_5 = 0.32;
    private static final double RATE_BRACKET_6 = 0.35;

    @Override
    public double calculateContribution(double monthlySalary) {
        return calculateTax(monthlySalary);
    }

    public double calculateTax(double taxableIncome) {
        if (taxableIncome <= BRACKET_0) {
            return 0;
        } else if (taxableIncome <= BRACKET_1) {
            return (taxableIncome - BRACKET_0) * RATE_BRACKET_2;
        } else if (taxableIncome <= BRACKET_2) {
            return TAX_BRACKET_2 + (taxableIncome - BRACKET_1) * RATE_BRACKET_3;
        } else if (taxableIncome <= BRACKET_3) {
            return TAX_BRACKET_3 + (taxableIncome - BRACKET_2) * RATE_BRACKET_4;
        } else if (taxableIncome <= BRACKET_4) {
            return TAX_BRACKET_4 + (taxableIncome - BRACKET_3) * RATE_BRACKET_5;
        } else {
            return TAX_BRACKET_5 + (taxableIncome - BRACKET_4) * RATE_BRACKET_6;
        }
    }

    public double calculateTax(double monthlySalary, double sssDeduction,
                               double philhealthDeduction, double pagibigDeduction) {
        double taxableIncome = monthlySalary - (sssDeduction + philhealthDeduction + pagibigDeduction);
        return calculateTax(taxableIncome);
    }

    @Override
    public String getName() {
        return "Withholding Tax";
    }

    @Override
    public boolean appliesTo(int payPeriodType) {
        return payPeriodType == DeductionProvider.END_MONTH;
    }

    public double computeTax(double taxableIncome) {
        if (taxableIncome <= 20833) return 0;
        else if (taxableIncome <= 33333) return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome <= 66667) return (taxableIncome - 33333) * 0.25 + 2500;
        else return (taxableIncome - 66667) * 0.32 + 10833.33;
    }
}