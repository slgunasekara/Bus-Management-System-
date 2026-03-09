package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;

public class DailyProfit {
    private LocalDate date;
    private double totalIncome;
    private double tripExpenses;
    private double salaries;
    private double maintenance;
    private double partPurchases;
    private double otherServices;
    private double totalExpenses;
    private double netProfit;
    private int totalTrips;

    public DailyProfit() {
    }

    public DailyProfit(LocalDate date, double totalIncome, double tripExpenses,
                          double salaries, double maintenance, double partPurchases,
                          double otherServices, double totalExpenses, double netProfit,
                          int totalTrips) {
        this.date = date;
        this.totalIncome = totalIncome;
        this.tripExpenses = tripExpenses;
        this.salaries = salaries;
        this.maintenance = maintenance;
        this.partPurchases = partPurchases;
        this.otherServices = otherServices;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
        this.totalTrips = totalTrips;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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


    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
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

    // DailyProfit entity ේ (pre-existing class)
    public double getTotalExpenses() {
        return tripExpenses + salaries + maintenance + partPurchases + otherServices;
    }

    public double getNetProfit() {
        return totalIncome - getTotalExpenses();
    }
}
