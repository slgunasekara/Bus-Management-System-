package lk.ijse.busmanagementsystem.tm;

public class MonthlyProfitTM {
    private String month;
    private String totalIncome;
    private String tripExpenses;
    private String salaries;
    private String maintenance;
    private String partPurchases;
    private String otherServices;
    private String totalExpenses;
    private String netProfit;

    public MonthlyProfitTM() {
    }

    public MonthlyProfitTM(String month, String totalIncome, String tripExpenses,
                           String salaries, String maintenance, String partPurchases,
                           String otherServices, String totalExpenses, String netProfit) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.tripExpenses = tripExpenses;
        this.salaries = salaries;
        this.maintenance = maintenance;
        this.partPurchases = partPurchases;
        this.otherServices = otherServices;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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
        return "MonthlyProfitTM{" +
                "month='" + month + '\'' +
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