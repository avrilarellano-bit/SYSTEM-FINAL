package motorph.deductions;

import java.util.ArrayList;
import java.util.List;

public class StatutoryDeductions {
    public static final int MID_MONTH = DeductionProvider.MID_MONTH;
    public static final int END_MONTH = DeductionProvider.END_MONTH;

    private static final DeductionProvider sssDeduction = new SSS();
    private static final PhilHealth philHealthDeduction = new PhilHealth();
    private static final PagIBIG pagIbigDeduction = new PagIBIG();
    private static final WithholdingTax taxDeduction = new WithholdingTax();

    private static final List<DeductionProvider> allDeductions = new ArrayList<>();

    static {
        allDeductions.add(sssDeduction);
        allDeductions.add(philHealthDeduction);
        allDeductions.add(pagIbigDeduction);
        allDeductions.add(taxDeduction);
    }

    public static DeductionResult calculateDeductions(double grossSalary, int payPeriod, double fullMonthlyGross) {
        double sssDeduction = 0;
        double philhealthDeduction = 0;
        double pagibigDeduction = 0;
        double withholdingTax = 0;

        if (payPeriod == MID_MONTH) {
            sssDeduction = StatutoryDeductions.sssDeduction.calculateContribution(fullMonthlyGross);
            philhealthDeduction = StatutoryDeductions.philHealthDeduction.calculateContribution(fullMonthlyGross) / 2;
            pagibigDeduction = StatutoryDeductions.pagIbigDeduction.calculateContribution(fullMonthlyGross);
        }

        if (payPeriod == END_MONTH) {
            double monthlySSS = StatutoryDeductions.sssDeduction.calculateContribution(fullMonthlyGross);
            double monthlyPhilHealth = StatutoryDeductions.philHealthDeduction.calculateContribution(fullMonthlyGross);
            double monthlyPagIBIG = StatutoryDeductions.pagIbigDeduction.calculateContribution(fullMonthlyGross);

            withholdingTax = StatutoryDeductions.taxDeduction.calculateTax(
                    fullMonthlyGross,
                    monthlySSS,
                    monthlyPhilHealth,
                    monthlyPagIBIG
            );
        }

        double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction + withholdingTax;

        return new DeductionResult(
                sssDeduction,
                philhealthDeduction,
                pagibigDeduction,
                withholdingTax,
                totalDeductions
        );
    }

    public static class DeductionResult {
        public final double sssDeduction;
        public final double philhealthDeduction;
        public final double pagibigDeduction;
        public final double withholdingTax;
        public final double totalDeductions;

        public DeductionResult(
                double sssDeduction,
                double philhealthDeduction,
                double pagibigDeduction,
                double withholdingTax,
                double totalDeductions
        ) {
            this.sssDeduction = sssDeduction;
            this.philhealthDeduction = philhealthDeduction;
            this.pagibigDeduction = pagibigDeduction;
            this.withholdingTax = withholdingTax;
            this.totalDeductions = totalDeductions;
        }
    }
}