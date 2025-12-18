package lk.ijse.busmanagementsystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal totalSalary;
    private BigDecimal netProfit;
    private int totalTrips;
    private LocalDate fromDate;
    private LocalDate toDate;

    public ReportDTO() {
    }

    public ReportDTO(BigDecimal totalIncome, BigDecimal totalExpenses, BigDecimal totalSalary,
                     BigDecimal netProfit, int totalTrips, LocalDate fromDate, LocalDate toDate) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.totalSalary = totalSalary;
        this.netProfit = netProfit;
        this.totalTrips = totalTrips;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(BigDecimal netProfit) {
        this.netProfit = netProfit;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "ReportDTO{" +
                "totalIncome=" + totalIncome +
                ", totalExpenses=" + totalExpenses +
                ", totalSalary=" + totalSalary +
                ", netProfit=" + netProfit +
                ", totalTrips=" + totalTrips +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                '}';
    }
}