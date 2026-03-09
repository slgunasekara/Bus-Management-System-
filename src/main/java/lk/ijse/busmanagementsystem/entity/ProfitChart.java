package lk.ijse.busmanagementsystem.entity;

import java.time.LocalDate;

public class ProfitChart {

    private LocalDate date;
    private double income;
    private double expense;
    private double profit;


    public ProfitChart() {
    }


    public ProfitChart(LocalDate date, double income, double expense, double profit) {
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.profit = profit;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    //Get formatted date string for chart X-axis (MM-dd format)

    public String getFormattedDate() {
        return String.format("%02d-%02d", date.getMonthValue(), date.getDayOfMonth());
    }

    //Get full formatted date string (yyyy-MM-dd format)
    public String getFullFormattedDate() {
        return date.toString();
    }



}
