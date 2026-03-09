package lk.ijse.busmanagementsystem.entity;

public class Dashboard {

    private int totalBuses;
    private int totalTrips;
    private int totalEmployees;
    private double totalIncome;
    private double totalExpenses;
    private double netProfit;


    public Dashboard() {
    }


    public Dashboard(int totalBuses, int totalTrips, int totalEmployees,
                        double totalIncome, double totalExpenses, double netProfit) {
        this.totalBuses = totalBuses;
        this.totalTrips = totalTrips;
        this.totalEmployees = totalEmployees;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }


    public int getTotalBuses() {
        return totalBuses;
    }

    public void setTotalBuses(int totalBuses) {
        this.totalBuses = totalBuses;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
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

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

}
