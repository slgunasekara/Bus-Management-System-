package lk.ijse.busmanagementsystem.dto;

public class DailyProfitDTO {
    private String date;
    private double totalIncome;
    private double tripExpenses;
    private double salaries;
    private double maintenance;
    private double partPurchases;
    private double otherServices;
    private double totalExpenses;
    private double netProfit;

    public DailyProfitDTO() {}

    public DailyProfitDTO(String date, double totalIncome, double tripExpenses,
                          double salaries, double maintenance, double partPurchases,
                          double otherServices, double totalExpenses, double netProfit) {
        this.date = date;
        this.totalIncome = totalIncome;
        this.tripExpenses = tripExpenses;
        this.salaries = salaries;
        this.maintenance = maintenance;
        this.partPurchases = partPurchases;
        this.otherServices = otherServices;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getTripExpenses() {
        return tripExpenses;
    }

    public void setTripExpenses(double tripExpenses) {
        this.tripExpenses = tripExpenses;
    }

    public double getSalaries() {
        return salaries;
    }

    public void setSalaries(double salaries) {
        this.salaries = salaries;
    }

    public double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(double maintenance) {
        this.maintenance = maintenance;
    }

    public double getPartPurchases() {
        return partPurchases;
    }

    public void setPartPurchases(double partPurchases) {
        this.partPurchases = partPurchases;
    }

    public double getOtherServices() {
        return otherServices;
    }

    public void setOtherServices(double otherServices) {
        this.otherServices = otherServices;
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

    @Override
    public String toString() {
        return "DailyProfitDTO{" +
                "date='" + date + '\'' +
                ", totalIncome=" + totalIncome +
                ", tripExpenses=" + tripExpenses +
                ", salaries=" + salaries +
                ", maintenance=" + maintenance +
                ", partPurchases=" + partPurchases +
                ", otherServices=" + otherServices +
                ", totalExpenses=" + totalExpenses +
                ", netProfit=" + netProfit +
                '}';
    }
}