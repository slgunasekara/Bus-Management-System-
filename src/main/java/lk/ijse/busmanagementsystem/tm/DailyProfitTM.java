package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class DailyProfitTM {
    private LocalDate date;
    private String totalIncome;
    private String tripExpenses;
    private String salaries;
    private String maintenance;
    private String partPurchases;
    private String otherServices;
    private String totalExpenses;
    private String netProfit;

    public DailyProfitTM() {
    }

    public DailyProfitTM(LocalDate date, String totalIncome, String tripExpenses,
                         String salaries, String maintenance, String partPurchases,
                         String otherServices, String totalExpenses, String netProfit) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTripExpenses() {
        return tripExpenses;
    }

    public void setTripExpenses(String tripExpenses) {
        this.tripExpenses = tripExpenses;
    }

    public String getSalaries() {
        return salaries;
    }

    public void setSalaries(String salaries) {
        this.salaries = salaries;
    }

    public String getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(String maintenance) {
        this.maintenance = maintenance;
    }

    public String getPartPurchases() {
        return partPurchases;
    }

    public void setPartPurchases(String partPurchases) {
        this.partPurchases = partPurchases;
    }

    public String getOtherServices() {
        return otherServices;
    }

    public void setOtherServices(String otherServices) {
        this.otherServices = otherServices;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(String totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(String netProfit) {
        this.netProfit = netProfit;
    }

    @Override
    public String toString() {
        return "DailyProfitTM{" +
                "date=" + date +
                ", totalIncome='" + totalIncome + '\'' +
                ", tripExpenses='" + tripExpenses + '\'' +
                ", salaries='" + salaries + '\'' +
                ", maintenance='" + maintenance + '\'' +
                ", partPurchases='" + partPurchases + '\'' +
                ", otherServices='" + otherServices + '\'' +
                ", totalExpenses='" + totalExpenses + '\'' +
                ", netProfit='" + netProfit + '\'' +
                '}';
    }
}
