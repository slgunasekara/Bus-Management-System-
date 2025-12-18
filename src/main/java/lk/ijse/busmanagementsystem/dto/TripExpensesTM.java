package lk.ijse.busmanagementsystem.dto;

import lk.ijse.busmanagementsystem.enums.TripExpType;
import java.time.LocalDate;

public class TripExpensesTM {
    private int tripExpId;
    private int tripId;
    private TripExpType tripExpType;
    private Integer salaryId;
    private double amount;
    private String description;
    private LocalDate date;
    private String createdByUsername;

    public TripExpensesTM() {
    }

    public TripExpensesTM(int tripExpId, int tripId, TripExpType tripExpType,
                          Integer salaryId, double amount, String description,
                          LocalDate date, String createdByUsername) {
        this.tripExpId = tripExpId;
        this.tripId = tripId;
        this.tripExpType = tripExpType;
        this.salaryId = salaryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.createdByUsername = createdByUsername;
    }

    // Getters and Setters
    public int getTripExpId() {
        return tripExpId;
    }

    public void setTripExpId(int tripExpId) {
        this.tripExpId = tripExpId;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public TripExpType getTripExpType() {
        return tripExpType;
    }

    public void setTripExpType(TripExpType tripExpType) {
        this.tripExpType = tripExpType;
    }

    public Integer getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    @Override
    public String toString() {
        return "TripExpensesTM{" +
                "tripExpId=" + tripExpId +
                ", tripId=" + tripId +
                ", tripExpType=" + tripExpType +
                ", salaryId=" + salaryId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", createdByUsername='" + createdByUsername + '\'' +
                '}';
    }
}