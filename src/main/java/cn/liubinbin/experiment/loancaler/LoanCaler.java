package cn.liubinbin.experiment.loancaler;

public class LoanCaler {

    public static void main(String[] args) {
        double principal = 1430000;  // 贷款本金
        double annualInterestRate = 0.042;  // 年利率
        int months = 28 * 12;  // 还款月数

        double monthlyInterestRate = annualInterestRate / 12;
        double monthlyPrincipal = principal / months;
        printEqualPrincipalDetails(principal, monthlyInterestRate, months, monthlyPrincipal);
    }

    private static void printEqualPrincipalDetails(double principal, double monthlyInterestRate, int months, double monthlyPrincipal) {
        double remainingBalance = principal;
        for (int month = 1; month <= months; month++) {
            double interest = remainingBalance * monthlyInterestRate;
            remainingBalance -= monthlyPrincipal;

            System.out.printf("Month %d: Principal Paid %.2f, Interest %.2f, Remaining Balance %.2f%n",
                    month, monthlyPrincipal, interest, remainingBalance);
        }
    }
}
