package lk.ijse.busmanagementsystem.dto;

import java.time.LocalDate;

public class ExpenseTM {
    private LocalDate expenseDate;
    private double amount;
    private String category;

    public ExpenseTM() {
    }

    public ExpenseTM(LocalDate expenseDate, double amount, String category) {
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.category = category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ExpenseTM{" +
                "expenseDate=" + expenseDate +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                '}';
    }
}