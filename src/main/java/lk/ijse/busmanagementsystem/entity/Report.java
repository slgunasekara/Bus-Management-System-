package lk.ijse.busmanagementsystem.entity;

public class Report {

    private double totalIncome;
    private double totalExpenses;
    private double totalSalary;
    private double netProfit;
    private int totalTrips;

    public Report() {
    }

    public Report(double totalIncome, double totalExpenses, double totalSalary, double netProfit, int totalTrips) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.totalSalary = totalSalary;
        this.netProfit = netProfit;
        this.totalTrips = totalTrips;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }
}
